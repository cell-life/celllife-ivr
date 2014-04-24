package org.celllife.ivr.application.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.CronTriggerBean;

import java.util.Date;
import java.util.List;

public interface QuartzService {

    /**
     * Clears all the quartz triggers in a group;
     * @param groupName Name of the quartz trigger group.
     */
    void clearTriggersForGroup(String groupName);

    /**
     * Gets all the quartz triggers in a group.
     * @param groupName Name of the quartz trigger group.
     * @return
     */
    List<String> getTriggers(String groupName);

    /**
     * Adds a quartz trigger.
     * @param trigger Trigger to add.
     * @throws Exception
     */
    void addTrigger(CronTriggerBean trigger) throws SchedulerException;

    /**
     * Removes a quartz trigger.
     * @param triggerName Name of the trigger.
     * @param groupName Name of the quartz trigger group.
     * @throws SchedulerException
     */
    void removeTrigger(String triggerName, String groupName) throws SchedulerException;

    /**
     * Generates a Cron expression for a daily occurrence.
     * @param msgDateTime
     * @return
     */
    String generateCronExprForDailyOccurence(Date msgDateTime);

    /**
     * Get the scheduler for the quartz service.
     * @return Scheuler for the quartz service.
     */
    Scheduler getScheduler();

}
