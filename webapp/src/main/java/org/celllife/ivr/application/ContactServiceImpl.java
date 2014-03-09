package org.celllife.ivr.application;

import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.contact.ContactRepository;
import org.celllife.ivr.domain.exception.ContactExistsException;
import org.celllife.ivr.domain.exception.IvrException;
import org.dozer.util.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private static Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Autowired
    ContactRepository contactRepository;

    @Override
    public List<Contact> getAllContacts() {
        return IteratorUtils.toList(contactRepository.findAll().iterator());
    }

    @Override
    public List<Contact> findContactsInCampaign(Long campaignId) {
        return IteratorUtils.toList(contactRepository.findContactsInCampaign(campaignId).iterator());
    }

    @Override
    public Contact saveContact(Contact contact) throws ContactExistsException {
        if ((contact.getId() == null)) {
            if (msisdnExists(contact.getMsisdn())) {
                log.warn("A contact with msisdn " + contact.getMsisdn() + " already exists.");
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
    public void saveContacts(List<Contact> contacts) throws Exception {
        List<String> failedContacts = new ArrayList<>();
        for (Contact contact : contacts) {
            try {
                saveContact(contact);
            }
            catch (ContactExistsException e) {
                failedContacts.add(contact.getMsisdn());
            }
        }
        if (failedContacts.size() > 0) {
            throw new IvrException("Some contacts could not be saved because their msisdns already exist.");
        }
    }

    @Override
    public void deleteAll() {
        contactRepository.deleteAll();
    }

    @Override
    public List<Contact> findContactByMsisdn(String msisdn) {
        return IteratorUtils.toList(contactRepository.findContactByMsisdn(msisdn).iterator());
    }

    @Override
    public boolean msisdnExists(String msisdn) {
        if (findContactByMsisdn(msisdn).size() >= 1) {
            return true;
        }
        return false;
    }

}
