package org.celllife.ivr.application;

import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

public interface QuartzService {

    void clearTriggersForGroup(String groupName);

    List<String> getTriggers(String groupName);

    void addTrigger(CronTriggerBean trigger) throws Exception;

    void removeTrigger(String triggerName, String groupName) throws SchedulerException;

    String cronExprForDailyOccurence(Date msgDateTime);

}
