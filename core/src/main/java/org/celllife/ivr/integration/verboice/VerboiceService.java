package org.celllife.ivr.integration.verboice;

import org.celllife.ivr.domain.exception.IvrException;

public interface VerboiceService {

    /**
     * Enqueus a call to Verboice with no password. A message number variable is sent.
     * @param channelName
     * @param callFlowName
     * @param scheduleName
     * @param msisdn
     * @param messageNumber
     * @return The response string from Verboice. Format {"call_id":58,"state":"queued"} - see https://bitbucket.org/instedd/verboice/wiki/API.wiki
     * @throws Exception
     */
    String enqueueCall(String channelName, String callFlowName, String scheduleName, String msisdn, int messageNumber) throws Exception;

    /**
     * Enqueues a call to Verboice with a password variable and a message number variable.
     * @param channelName
     * @param callFlowName
     * @param scheduleName
     * @param msisdn
     * @param messageNumber
     * @param password
     * @return The response string from Verboice. Format {"call_id":58,"state":"queued"} - see https://bitbucket.org/instedd/verboice/wiki/API.wiki
     * @throws IvrException
     */
    String enqueueCallWithPassword(String channelName, String callFlowName, String scheduleName, String msisdn, int messageNumber, String password) throws IvrException;

}
