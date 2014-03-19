package org.celllife.ivr.application.quartz;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

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
                boolean unscheduleJob = scheduler.unscheduleJob(trigger, groupName);
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
            String[] triggers = scheduler.getTriggerNames(groupName);
            CollectionUtils.addAll(list, triggers);
            return list;
        } catch (SchedulerException e) {
            log.error("Error checking triggers [group=" + groupName + "]", e);
        }
        return list;
    }

    private boolean doesTriggerExist(String triggerGroup, String triggerName) {

        try {
            Trigger triggerNames = scheduler.getTrigger(triggerName, triggerGroup);
            return triggerNames != null;
        } catch (SchedulerException e) {
            log.error("Error checking triggers [name=" + triggerName + "] [group=" + triggerGroup + "]", e);
        }

        return false;
    }

    @Override
    public void removeTrigger(String triggerName, String groupName) throws SchedulerException {
        scheduler.unscheduleJob(triggerName, groupName);
    }

    @Override
    public void addTrigger(CronTriggerBean trigger) throws Exception {
        try {
            scheduler.scheduleJob(trigger);
        } catch (Exception e) {
            throw new Exception("Error scheduling job. Cause: " + e.getMessage(), e);
        }
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

}
