package org.celllife.ivr.application;

import org.celllife.ivr.domain.Contact;
import org.celllife.ivr.domain.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepository contactRepository;

    public Iterable<Contact> getAllContacts() {

        return contactRepository.findAll();

    }

    public void saveContact(Contact contact) {

        contactRepository.save(contact);

    }

    public void deleteAll() {

        contactRepository.deleteAll();

    }

}
