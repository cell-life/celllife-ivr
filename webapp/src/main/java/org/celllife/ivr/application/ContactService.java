package org.celllife.ivr.application;

import org.celllife.ivr.domain.contact.Contact;

import java.util.List;

public interface ContactService {

    public List<Contact> getAllContacts();

    public Contact saveContact(Contact contact) throws Exception;

    public void deleteAll();

    public void saveContacts(List<Contact> contacts) throws Exception;

    public List<Contact> findContactsInCampaign(Long campaignId);

    public List<Contact> findContactByMsisdn(String msisdn);

    public boolean msisdnExists(String msisdn);

}
