package org.celllife.ivr.application;

import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.ContactExistsException;

import java.util.List;

public interface ContactService {

    public List<Contact> getAllContacts();

    public Contact saveContact(Contact contact) throws ContactExistsException;

    public void deleteAll();

    public List<String> saveContacts(List<Contact> contacts);

    public List<Contact> findContactsInCampaign(Long campaignId);

    public List<Contact> findContactByMsisdn(String msisdn);

    public boolean msisdnExists(String msisdn);

}
