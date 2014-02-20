package org.celllife.ivr.application;

import org.celllife.ivr.domain.Contact;

public interface ContactService {

    public Iterable<Contact> getAllContacts();

    public void saveContact(Contact contact);

    public void deleteAll();

}
