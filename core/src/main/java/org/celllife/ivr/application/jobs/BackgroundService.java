package org.celllife.ivr.application.jobs;

public interface BackgroundService {

    /**
     * This function will retry all failed calls from the current day.
     */
    void findFailedCallsAndRetry();

}
