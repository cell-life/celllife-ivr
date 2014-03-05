package org.celllife.ivr.application;

import org.celllife.ivr.domain.contact.Contact;

import java.util.List;

public interface ContactService {

    public List<Contact> getAllContacts();

    public void saveContact(Contact contact);

    public void deleteAll();

    public void saveContacts(List<Contact> contacts);

}
