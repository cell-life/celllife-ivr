package org.celllife.ivr.application.verboice;

import org.celllife.ivr.domain.exception.IvrException;

public interface VerboiceApplicationService {

    /**
     * Enqueues a call to the Verboice server.
     *
     * @param channelName Name of the channel to use, on the Verboice server.
     * @param callFlowName Name of the call flow to use, on the Verboice server.
     * @param scheduleName Name of the schedule to use, on the Verboice server.
     * @param msisdn Phone number to enqueue a call for.
     * @param messageNumber Message number to use, in the Verboice call flow on the Verboice server.
     * @throws IvrException
     */
    void enqueueCallForMsisdn(String channelName, String callFlowName, String scheduleName, String msisdn, int messageNumber, String password);

}
