package org.celllife.ivr.application.contact;

import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.ContactExistsException;

import java.util.List;

public interface ContactService {

    /**
     * Gets all contacts in the system.
     * @return A list of contacts.
     */
    public List<Contact> getAllContacts();

    /**
     * Saves a contact.
     * @param contact The contact to save.
     * @return The updated contact.
     * @throws ContactExistsException
     */
    public Contact saveContact(Contact contact) throws ContactExistsException;

    /**
     * Delete all contacts.
     */
    public void deleteAll();

    /**
     * Save contacts.
     * @param contacts Contacts to save.
     * @return List of msisdns of contacts that failed to save.
     */
    public List<String> saveContacts(List<Contact> contacts);

    /**
     * Find contacts in a particular campaign.
     * @param campaignId The ID of the campaign to find contacts for.
     * @return List of contacts.
     */
    public List<Contact> findContactsInCampaign(Long campaignId);

    /**
     * Find contacts by msisdn.
     * @param msisdn The msisdn to find contacts for.
     * @return List of contacts.
     */
    public List<Contact> findContactByMsisdn(String msisdn);

    /**
     * Check whether a contact with said msisdn exists.
     * @param msisdn The msisdn to check.
     * @return True if it exists, false if it doesn't exist.
     */
    public boolean msisdnExists(String msisdn);

}
