package org.celllife.ivr.application.campaign;

import org.celllife.ivr.application.jobs.RelativeCampaignJobRunner;
import org.celllife.ivr.application.message.CampaignMessageService;
import org.celllife.ivr.application.quartz.QuartzService;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.campaign.CampaignRepository;
import org.celllife.ivr.domain.exception.CampaignNameExistsException;
import org.celllife.ivr.domain.exception.IvrException;
import org.celllife.ivr.domain.message.CampaignMessage;
import org.celllife.ivr.domain.message.CampaignMessageDto;
import org.dozer.util.IteratorUtils;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.quartz.TriggerBuilder.newTrigger;

@Service
@Transactional("transactionManager")
public class CampaignServiceImpl implements CampaignService {

    private static Logger log = LoggerFactory.getLogger(CampaignServiceImpl.class);

    @Autowired
    CampaignMessageService campaignMessageService;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    QuartzService quartzService;

    @Override
    public void setMessagesForDailyCampaign(Long campaignId, List<Integer> verboiceMessageNumbers, List<Date> messageTimesOfDay) throws IvrException {

        Campaign campaign = getCampaign(campaignId);

        List<CampaignMessage> oldCampaignMessages = campaignMessageService.findMessagesInCampaign(campaignId);

        List<CampaignMessage> campaignMessages = new ArrayList<>();

        int messageCounter = 0;
        for (int i = 0; i < campaign.getDuration(); i++) {
            int msgSlot = 1;
            int msgDay = i + 1;
            CampaignMessage newCampaignMessage = new CampaignMessage(verboiceMessageNumbers.get(messageCounter), msgDay, msgSlot, messageTimesOfDay.get(0), campaignId, msgDay);
            campaignMessages.add(newCampaignMessage);
            campaignMessageService.save(newCampaignMessage);
            messageCounter++;
        }

        createTriggersForMessages(campaignMessages, campaign);

        for (CampaignMessage campaignMessage : oldCampaignMessages) {
            campaignMessageService.deleteMessage(campaignMessage.getId());
        }
    }

    @Override
    public List<CampaignMessage> setMessagesForCampaign(Long campaignId, List<CampaignMessageDto> campaignMessageDtos) throws IvrException {

        Campaign campaign = getCampaign(campaignId);
        List<CampaignMessage> oldCampaignMessages = campaignMessageService.findMessagesInCampaign(campaignId);
        List<CampaignMessage> campaignMessages = new ArrayList<>();
        Integer sequenceNumber = 0;

        // Create new messages, which includes calculating their sequences.
        for (int i = 0; i < campaign.getDuration(); i++) {
            int msgDay = i + 1;
            List<CampaignMessageDto> campaignMessageDtosForDay = findAndSortCampaignMessagesForDay(msgDay, campaignMessageDtos);

            for (int j = 0; j < campaignMessageDtosForDay.size(); j++) {
                sequenceNumber = sequenceNumber + 1;
                int msgSlot = j + 1;
                CampaignMessage newCampaignMessage = null;
                try {
                    newCampaignMessage = new CampaignMessage(campaignMessageDtosForDay.get(j).getVerboiceMessageNumber(), msgDay, msgSlot, convertStringToHoursMinutes(campaignMessageDtosForDay.get(j).getMessageTimeOfDay()), campaignId, sequenceNumber);
                } catch (ParseException e) {
                    throw new IvrException(e.getLocalizedMessage() + "The message time " + campaignMessageDtosForDay.get(j).getMessageTimeOfDay() + " is invalid. Times must be in format HH:mm");
                }
                campaignMessages.add(newCampaignMessage);
                campaignMessageService.save(newCampaignMessage);
            }

        }

        createTriggersForMessages(campaignMessages, campaign); // Creates new quartz triggers for messages.

        for (CampaignMessage campaignMessage : oldCampaignMessages) {
            campaignMessageService.deleteMessage(campaignMessage.getId());
        }

        return campaignMessages;
    }

    /* Creates quartz triggers for messages and deletes existing quartz triggers.
       Trigger properties:
            Trigger Name - format: CampaignName-[slot=n]-[time=HH:mm:ss aa] eg. "Test Campaign-[slot=1]-[time=12:00:00 PM]" [CampaignNa
            Trigger Group - format: org.celllife.ivr.domain.campaign.Campaign:1
            JobData - PROP_CAMPAIGN_ID, PROP_MSGTIME, PROP_MSGSLOT
            Job Name - "relativeCampaignJobRunner"
            Job Group - "campaignJobs"
            CronExpression
    */
    protected void createTriggersForMessages(List<CampaignMessage> campaignMessages, Campaign campaign) throws IvrException {

        String group = campaign.getIdentifierString();
        List<String> existingTriggers = quartzService.getTriggers(group);
        List<String> newTriggers = new ArrayList<>();

        for (CampaignMessage campaignMessage : campaignMessages) {

            String name = getTriggerNameForCampaign(campaign.getId(), campaignMessage.getMessageTime(), campaignMessage.getMessageSlot());

            if (existingTriggers.contains(name) || newTriggers.contains(name)) {
                log.debug("Trigger [{}] already exists for campaign [{}]", name, campaign.getId());
                existingTriggers.remove(name);
            } else {
                log.debug("MsgTime [{}] for campaign [{}] for msgSlot [{}]", new Object[]{campaignMessage.getMessageTime(), campaign.getId(), campaignMessage.getMessageSlot()});
                String triggerName = null;
                try {
                    triggerName = createQuartzTriggerForCampaign(campaign.getId(), campaignMessage.getMessageTime(), campaignMessage.getMessageSlot());
                    newTriggers.add(triggerName);
                } catch (ParseException e) {
                    log.warn("The message time " + campaignMessage.getMessageTime() + " is invalid. Times must be in format HH:mm. Trigger for this message will not be added.");
                } catch (SchedulerException e) {
                    log.warn("Could not schedule trigger for campaign message with id " + campaignMessage.getId() + ". Reason: " + e.getLocalizedMessage());
                }
            }
        }

        for (String oldTrigger : existingTriggers) {
            log.debug("Removing old trigger for campaign [id={}] [trigger={}]", campaign.getId(), oldTrigger);
            try {
                quartzService.removeTrigger(oldTrigger, campaign.getIdentifierString());
            } catch (SchedulerException e) {
                throw new IvrException(e.getLocalizedMessage() + " Reason: Could not remove trigger " + oldTrigger.toString());
            }
        }
    }

    // This finds all the messages for a particular day, and sorts them ascending order (i.e. earliest message first.)
    protected List<CampaignMessageDto> findAndSortCampaignMessagesForDay(Integer day, List<CampaignMessageDto> campaignMessageDtos) {

        List<CampaignMessageDto> dtosToReturn = new ArrayList<>();
        for (CampaignMessageDto campaignMessageDto : campaignMessageDtos) {
            if (campaignMessageDto.getMessageDay() == day)
                dtosToReturn.add(campaignMessageDto);
        }

        Collections.sort(dtosToReturn, new Comparator<CampaignMessageDto>() {

            public int compare(CampaignMessageDto cm1, CampaignMessageDto cm2) {
                try {
                    Date date1 = convertStringToHoursMinutes(cm1.getMessageTimeOfDay());
                    Date date2 = convertStringToHoursMinutes(cm2.getMessageTimeOfDay());
                    if (date1.before(date2))
                        return -1;
                    else if (date1.after(date2))
                        return 1;
                    else if (date1.equals(date2))
                        return 0;
                } catch (ParseException e) {
                    return 1;
                }
                return 1;
            }
        });

        return dtosToReturn;

    }

    protected Date convertStringToHoursMinutes(String messageTime) throws ParseException {

        DateFormat formatter = new SimpleDateFormat("HH:mm");
        return ((Date) formatter.parse(messageTime));

    }

    private String createQuartzTriggerForCampaign(Long campaignId, Date msgTime, Integer msgSlot) throws ParseException, SchedulerException {

        Campaign campaign = getCampaign(campaignId);

        JobDataMap jobMap = new JobDataMap();

        jobMap.put(RelativeCampaignJobRunner.PROP_CAMPAIGN_ID, campaignId);
        jobMap.put(RelativeCampaignJobRunner.PROP_MSGTIME, msgTime);

        if (msgSlot != null) {
            jobMap.put(RelativeCampaignJobRunner.PROP_MSGSLOT, msgSlot);
        }

        String name = getTriggerNameForCampaign(campaignId, msgTime, msgSlot);
        String cronExpr = quartzService.generateCronExprForDailyOccurence(msgTime);
        CronScheduleBuilder cronScheduleBuilder =  CronScheduleBuilder.cronSchedule(cronExpr);
        TriggerKey triggerKey = new TriggerKey(name,campaign.getIdentifierString());
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setName("relativeCampaignJobRunner");
        jobDetail.setGroup("campaignJobs");
        Trigger trigger = newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).usingJobData(jobMap).forJob(jobDetail).build();
        quartzService.addTrigger(trigger);
        log.debug("Campaign: {} scheduled with cron expression {}", campaignId, cronExpr);

        return name;
    }

    @Transactional(readOnly = true)
    public Campaign getCampaign(Long id) {
        return campaignRepository.findOne(id);
    }

    public Campaign saveCampaign(Campaign campaign) throws CampaignNameExistsException {

        if (campaign.getId() == null) {
            if (campaignNameExists(campaign.getName())) {
                throw new CampaignNameExistsException("Cannot save campaign. A campaign with the name " + campaign.getName() + " already exists.");
            } else
                return campaignRepository.save(campaign);
        } else
            return campaignRepository.save(campaign);
    }

    private String getTriggerNameForCampaign(Long campaignId, Date msgTime, Integer msgSlot) {
        Campaign campaign = getCampaign(campaignId);
        String name = MessageFormat.format("{0}-[slot={1}]-[time={2,time,medium}]", campaign.getName(), msgSlot, msgTime);
        return name;
    }

    public List<CronTrigger> findTriggerByJobNameAndGroup(String jobName, String jobGroup) throws SchedulerException {

        return quartzService.findTriggerByJobNameAndGroup(jobName, jobGroup);

    }

    @Override
    public void deleteAllCampaigns() {
        campaignRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Campaign> getAllCampaigns() {
        return IteratorUtils.toList(campaignRepository.findAll().iterator());
    }


    @Transactional(readOnly = true)
    @Override
    public List<Campaign> findCampaignByName(String name) {
        return IteratorUtils.toList(campaignRepository.findByName(name).iterator());
    }

    @Transactional(readOnly = true)
    public boolean campaignNameExists(String name) {
        List<Campaign> campaigns = IteratorUtils.toList(campaignRepository.findByName(name).iterator());
        if (campaigns.size() >= 1) {
            return true;
        }
        return false;
    }

    public void deleteAllTriggers() throws SchedulerException {

        Set<JobKey> jobkeys = quartzService.getScheduler().getJobKeys(GroupMatcher.jobGroupEquals("campaignJobs"));
        List<CronTrigger> triggers = new ArrayList<>();
        for (JobKey jobKey : jobkeys) {
            if (jobKey.getName().equals("relativeCampaignJobRunner"))
            {
                triggers = (List<CronTrigger>)quartzService.getScheduler().getTriggersOfJob(jobKey);
            }
        }
        for (Trigger trigger : triggers) {
            TriggerKey triggerKey = trigger.getKey();
            quartzService.getScheduler().unscheduleJob(triggerKey);
        }
    }

}
