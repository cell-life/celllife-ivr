package org.celllife.ivr.application.verboice;

import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.IvrException;

import java.util.List;

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

    /**
     * Saves a contact to the repository.
     * @param verboiceContact Contact to save.
     * @return The newly created contact.
     */
    //Contacts saveVerboiceContact(Contacts verboiceContact);

    /**
     * Creates Verboice contacts, based on Cell Life IVR contacts, and saves them.
     * @param contacts The Cell Life contacts to convert.
     * @param campaignId The campaign in question.
     * @return The msisdns of contacts that failed to be saved.
     * @throws IvrException
     */
    //List<String> createContactsAndSave(List<Contact> contacts, Long campaignId) throws IvrException;

}
