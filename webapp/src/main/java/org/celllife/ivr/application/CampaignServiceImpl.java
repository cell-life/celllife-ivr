package org.celllife.ivr.application;

import org.celllife.ivr.domain.*;
import org.celllife.ivr.framework.campaign.RelativeCampaignJobRunner;
import org.dozer.util.IteratorUtils;
import org.quartz.CronExpression;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
public class CampaignServiceImpl implements CampaignService{

    private static Logger log = LoggerFactory.getLogger(CampaignServiceImpl.class);

    @Autowired
    CampaignMessageService campaignMessageService;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    QuartzService quartzService;

    @Override
    public void setMessagesForCampaign(Long campaignId, List<Integer> verboiceMessageNumbers, List<Date> messageTimesOfDay) throws Exception {

        Campaign campaign = getCampaign(campaignId);

        //FIXME: need to figure out whether I need this yet and where to put it.
        /*campaign.setEndDate(null);
        if (campaign.getStartDate() == null) {
            campaign.setStartDate(new Date());
        }
        campaign.setStatus(CampaignStatus.ACTIVE);
        campaignRepository.save(campaign); */

        List<CampaignMessage> campaignMessages = new ArrayList<>();

        int messageCounter = 0;
        for (int i = 0; i < campaign.getDuration(); i++) {
            for (int j = 0; j < campaign.getTimesPerDay(); j++) {
                int msgSlot = j + 1;
                int msgDay = i + 1;
                CampaignMessage newCampaignMessage = new CampaignMessage(verboiceMessageNumbers.get(messageCounter), msgDay, msgSlot, messageTimesOfDay.get(j));
                campaignMessages.add(newCampaignMessage);
                campaignMessageService.save(newCampaignMessage);
                messageCounter++;
            }
        }

        String group = campaign.getIdentifierString();
        List<String> existingTriggers = quartzService.getTriggers(group);
        List<String> newTriggers = new ArrayList<>();

        for (CampaignMessage campaignMessage : campaignMessages) {

            String name =getTriggerNameForCampaign(campaignId,  campaignMessage.getMessageTime(), campaignMessage.getMessageSlot());

            if (existingTriggers.contains(name) || newTriggers.contains(name)) {
                log.debug("Trigger [{}] already exists for campaign [{}]", name, campaignId);
                existingTriggers.remove(name);
            }
            else {
                log.debug("MsgTime [{}] for campaign [{}] for msgSlot [{}]", new Object[] {campaignMessage.getMessageTime(), campaignId, campaignMessage.getMessageSlot()});
                String triggerName = createQuartzTriggerForCampaign(campaignId, campaignMessage.getMessageTime(), campaignMessage.getMessageSlot());
                newTriggers.add(triggerName);
            }
        }

        for (String oldTrigger : existingTriggers) {
            try {
                log.debug("Removing old trigger for campaign [id={}] [trigger={}]", campaignId, oldTrigger);
                quartzService.removeTrigger(oldTrigger, campaign.getIdentifierString());
            }
            catch (SchedulerException e) {
                throw new Exception("Error removing trigger for campaign. Cause: " + e.getMessage(), e);
            }
        }
    }

    private String createQuartzTriggerForCampaign(Long campaignId, Date msgTime, Integer msgSlot) throws Exception {

        Campaign campaign = getCampaign(campaignId);

        Map<String, Object> jobMap = new HashMap<String, Object>();

        jobMap.put(RelativeCampaignJobRunner.PROP_CAMPAIGN_ID, campaignId);
        jobMap.put(RelativeCampaignJobRunner.PROP_MSGTIME, msgTime);

        if (msgSlot != null) {
            jobMap.put(RelativeCampaignJobRunner.PROP_MSGSLOT, msgSlot);
        }

        String name = getTriggerNameForCampaign(campaignId, msgTime, msgSlot);
        CronTriggerBean trigger = new CronTriggerBean();
        trigger.setName(name);
        trigger.setJobDataAsMap(jobMap);
        trigger.setJobName("relativeCampaignJobRunner");
        trigger.setJobGroup("campaignJobs");
        trigger.setVolatility(false);
        trigger.setGroup(campaign.getIdentifierString());

        try {
            String cronExpr = quartzService.cronExprForDailyOccurence(msgTime);
            trigger.setCronExpression(new CronExpression(cronExpr));
            quartzService.addTrigger(trigger);
            log.debug("Campaign: {} scheduled with cron expression {}", campaignId, cronExpr);
        } catch (Exception e) {
            throw new Exception("Error scheduling campaign. Cause: " + e.getMessage(), e);
        }

        return name;
    }

    public Campaign getCampaign(Long id) {
        return campaignRepository.findOne(id);
    }

    public Campaign saveCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    private String getTriggerNameForCampaign(Long campaignId, Date msgTime, Integer msgSlot) {
        Campaign campaign = getCampaign(campaignId);
        String name = MessageFormat.format("{0}-[slot={1}]-[time={2,time,medium}]", campaign.getName(), msgSlot, msgTime);
        return name;
    }

    @Override
    public Scheduler getScheduler() {
        return quartzService.getScheduler();
    }

    @Override
    public void deleteAllCampaigns() {
        campaignRepository.deleteAll();
    }

    @Override
    public List<Campaign> findAllCampaigns() {
        return IteratorUtils.toList(campaignRepository.findAll().iterator());
    }

}
