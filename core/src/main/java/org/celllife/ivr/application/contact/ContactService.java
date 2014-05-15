package org.celllife.ivr.application.contact;

import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.ContactExistsException;

import java.util.List;

public interface ContactService {

    /**
     * Gets all contacts in the system.
     * @return A list of contacts.
     */
    List<Contact> getAllContacts();

    /**
     * Gets a contact by id.
     * @param id
     * @return
     */
    Contact getContactById(Long id);

    /**
     * Saves a contact.
     * @param contact The contact to save.
     * @return The updated contact.
     * @throws ContactExistsException
     */
    Contact saveContact(Contact contact) throws ContactExistsException;

    /**
     * Delete all contacts.
     */
    void deleteAll();

    /**
     * Save contacts.
     * @param contacts Contacts to save.
     * @return List of msisdns of contacts that failed to save.
     */
    List<String> saveContacts(List<Contact> contacts);

    /**
     * Find contacts in a particular campaign.
     * @param campaignId The ID of the campaign to find contacts for.
     * @return List of contacts.
     */
    List<Contact> findContactsInCampaign(Long campaignId);

    /**
     * Find all non voided (active) campaign contacts.
     * @param campaignId
     * @return
     */
    List<Contact> findNonVoidedContactsInCampaign(Long campaignId);

    /**
     * Find contacts by msisdn.
     * @param msisdn The msisdn to find contacts for.
     * @return List of contacts.
     */
    List<Contact> findContactByMsisdn(String msisdn);

    /**
     * Finds a contact by msisdn and campaign.
     * @param msisdn
     * @param campaignId
     * @return
     */
    Contact findContactByMsisdnAndCampaign(String msisdn, Long campaignId);

    /**
     * Check whether a contact with said msisdn exists.
     * @param msisdn The msisdn to check.
     * @return True if it exists, false if it doesn't exist.
     */
    boolean msisdnExists(String msisdn);

    /**
     * Checks whether a contact with a particular msisdn exists in a particular campaign.
     * @param msisdn The msisdn concerned.
     * @param campaignId The id of the campaign.
     * @return True of the contact exists and false if it doesn't.
     */
    boolean contactExists(String msisdn, Long campaignId);

}
