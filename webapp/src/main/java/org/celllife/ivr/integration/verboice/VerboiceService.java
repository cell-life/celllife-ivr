package org.celllife.ivr.integration.verboice;

import org.celllife.ivr.domain.Contact;

public interface VerboiceService {

    public String enqueueCall(String channelName, String callFlowName, String scheduleName, String msisdn, String password, int messageNumber) throws Exception;

    public String enqueueCall(String channelName, String callFlowName, String scheduleName, Contact contact, int messageNumber)  throws Exception;

}
