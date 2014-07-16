package org.celllife.ivr.application.quartz;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.Calendar;

@Service
public class QuartzServiceImpl implements QuartzService {

    private static final String DAILY_CRONEXPR = "{0} {1} {2} ? * *";

    private static Logger log = LoggerFactory.getLogger(QuartzServiceImpl.class);

    @Autowired
    @Qualifier("qrtzScheduler")
    private Scheduler scheduler;

    public String generateCronExprForDailyOccurence(Date msgDateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(msgDateTime);

        String cronExpr = MessageFormat.format(DAILY_CRONEXPR, new Object[]{
                calendar.get(Calendar.SECOND), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.HOUR_OF_DAY)});

        return cronExpr;
    }

    @Override
    public void clearTriggersForGroup(String groupName) {

        try {
            List<String> triggers = getTriggers(groupName);
            for (String trigger : triggers) {
                TriggerKey triggerKey = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(trigger)).iterator().next();
                boolean unscheduleJob = scheduler.unscheduleJob(triggerKey);
                if (unscheduleJob) {
                    log.debug("Trigger : [{}] deleted for group : [{}]", trigger, groupName);
                } else {
                    log.error("Trigger : [{}] NOT deleted for group : [{}]", trigger, groupName);
                }
            }
        } catch (SchedulerException e) {
            log.error("Error clearing triggers for group " + groupName, e);
        }

    }

    @Override
    public List<String> getTriggers(String groupName) {

        ArrayList<String> list = new ArrayList<String>();

        try {
            Set<TriggerKey> triggers = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName));
            for (TriggerKey triggerKey : triggers) {
                list.add(triggerKey.getName());
            }
            return list;
        } catch (SchedulerException e) {
            log.error("Error checking triggers [group=" + groupName + "]", e);
        }
        return list;
    }

    private boolean doesTriggerExist(String triggerGroup, String triggerName) {

        try {
            TriggerKey triggerKey = findTriggerKeyByTriggerGroupAndName(triggerName, triggerGroup);
            if (triggerKey != null)
                return true;
            else
                return false;
        } catch (SchedulerException e) {
            log.warn("Could not find trigger " + triggerName + " in group " + triggerGroup, e);
            return false;
        }
    }

    @Override
    public void removeTrigger(String triggerName, String groupName) throws SchedulerException {
        TriggerKey triggerKey = findTriggerKeyByTriggerGroupAndName(triggerName, groupName);
        if (triggerKey != null) {
            scheduler.unscheduleJob(triggerKey);
        } else {
            log.warn("Could not remove trigger " + triggerName + " in group " + groupName + " because it was not found.");
        }
    }

    @Override
    public void addTrigger(Trigger trigger) throws SchedulerException {
        scheduler.scheduleJob(trigger);

    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    protected TriggerKey findTriggerKeyByTriggerGroupAndName(String triggerName, String groupName) throws SchedulerException {
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName));
        for (TriggerKey triggerKey : triggerKeys) {
            if (triggerKey.getName().equals(triggerName)) {
                return triggerKey;
            }
        }
        return null;
    }

    public List<CronTrigger> findTriggerByJobNameAndGroup(String jobName, String jobGroup) throws SchedulerException {

        Set<JobKey> jobkeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroup));
        List<CronTrigger> triggers = new ArrayList<>();
        for (JobKey jobKey : jobkeys) {
            if (jobKey.getName().equals(jobName)) {
                return (List<CronTrigger>) getScheduler().getTriggersOfJob(jobKey);
            }
        }

        return null;

    }

}
