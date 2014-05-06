package org.celllife.ivr.application.verboice;

import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.exception.IvrException;

public interface VerboiceApplicationService {

    /**
     * Enqueues a call to the Verboice server.
     *
     * @param campaign The campaign for which this call is queued.
     * @param msisdn Phone number to enqueue a call for.
     * @param messageNumber Message number to use, in the Verboice call flow on the Verboice server.
     * @param password Call Password.
     * @throws IvrException
     */
    String enqueueCallForMsisdn(Campaign campaign, String msisdn, int messageNumber, String password);

}
