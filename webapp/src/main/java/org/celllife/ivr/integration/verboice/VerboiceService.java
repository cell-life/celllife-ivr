package org.celllife.ivr.integration.verboice;

import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.IvrException;

public interface VerboiceService {

    String enqueueCall(String channelName, String callFlowName, String scheduleName, String msisdn, int messageNumber) throws Exception;

    String enqueueCall(String channelName, String callFlowName, String scheduleName, Contact contact, int messageNumber)  throws Exception;

}
