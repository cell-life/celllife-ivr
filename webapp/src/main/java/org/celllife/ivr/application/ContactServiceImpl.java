package org.celllife.ivr.application;

import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.contact.ContactRepository;
import org.dozer.util.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepository contactRepository;

    @Override
    public List<Contact> getAllContacts() {
        return IteratorUtils.toList(contactRepository.findAll().iterator());
    }

    @Override
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public void saveContacts(List<Contact> contacts) {
        contactRepository.save(contacts);
    }

    @Override
    public void deleteAll() {
        contactRepository.deleteAll();
    }

}
