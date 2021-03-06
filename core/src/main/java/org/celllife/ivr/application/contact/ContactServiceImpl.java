package org.celllife.ivr.application.contact;

import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.contact.ContactRepository;
import org.celllife.ivr.domain.exception.ContactExistsException;
import org.dozer.util.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional("transactionManager")
public class ContactServiceImpl implements ContactService {

    private static Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Autowired
    ContactRepository contactRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Contact> getAllContacts() {
        return IteratorUtils.toList(contactRepository.findAll().iterator());
    }

    @Transactional(readOnly = true)
    @Override
    public Contact getContactById(Long id) {
        return contactRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Contact> findContactsInCampaign(Long campaignId) {
        return IteratorUtils.toList(contactRepository.findContactsInCampaign(campaignId).iterator());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Contact> findNonVoidedContactsInCampaign(Long campaignId) {
        return IteratorUtils.toList(contactRepository.findNonVoidedContactsInCampaign(campaignId).iterator());
    }

    @Override
    public Contact saveContact(Contact contact) throws ContactExistsException {
        if ((contact.getId() == null)) {
            if (contactExists(contact.getMsisdn(), contact.getCampaignId())) {
                log.warn("A contact with msisdn " + contact.getMsisdn() + " already exists in campaign " + contact.getCampaignId() + " and cannot be added to the database again.");
                throw new ContactExistsException("A contact with msisdn " + contact.getMsisdn() + " already exists.");
            } else {
                return contactRepository.save(contact);
            }
        }
        else {
            return contactRepository.save(contact);
        }
    }

    @Override
    public List<String> saveContacts(List<Contact> contacts) {
        List<String> failedContacts = new ArrayList<>();
        for (Contact contact : contacts) {
            try {
                saveContact(contact);
            }
            catch (ContactExistsException e) {
                failedContacts.add(contact.getMsisdn());
            }
        }
        return failedContacts;
    }

    @Override
    public void deleteAll() {
        contactRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Contact> findContactByMsisdn(String msisdn) {
        return IteratorUtils.toList(contactRepository.findContactByMsisdn(msisdn).iterator());
    }

    @Transactional(readOnly = true)
    @Override
    public Contact findContactByMsisdnAndCampaign(String msisdn, Long campaignId) {
        List<Contact> contacts = IteratorUtils.toList(contactRepository.findContactByMsisdnAndCampaign(msisdn,campaignId).iterator());
        if (contacts.size() != 1) {
            return null;
        } else {
            return contacts.get(0);
        }
    }

    @Override
    public boolean msisdnExists(String msisdn) {
        if (findContactByMsisdn(msisdn).size() >= 1) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean contactExists(String msisdn, Long campaignId) {
        List<Contact> contacts = IteratorUtils.toList(contactRepository.findContactByMsisdnAndCampaign(msisdn,campaignId).iterator());
        if (contacts.size() >= 1) {
            return true;
        }
        return false;
    }

}
