package org.celllife.ivr.application;

import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.IvrException;
import org.celllife.ivr.domain.verboice.contacts.Contacts;

import java.util.List;

public interface VerboiceApplicationService {

    void enqueueCallForMsisdn(String channelName, String callFlowName, String scheduleName, String msisdn, String password, int messageNumber) throws IvrException;

    Contacts createContact(Contacts contact);

    List<String> createContactsFromCelllifeContactsAndSave(List<Contact> contacts, Long campaignId) throws IvrException;

}
