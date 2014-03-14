package org.celllife.ivr.application;

import junit.framework.Assert;
import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.ContactExistsException;
import org.celllife.ivr.test.TestConfiguration;
import org.dozer.util.IteratorUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class ContactServiceTest extends TestConfiguration {

    @Autowired
    ContactService contactService;

    @Before
    public void setUp() throws ContactExistsException, Exception {
        contactService.deleteAll();
        contactService.saveContact(new Contact("27724194158","1234", 1L, 0));
    }

    @Test
    public void testGetAllContacts() {
        List<Contact> contactList = IteratorUtils.toList(contactService.getAllContacts().iterator());
        Assert.assertEquals(1,contactList.size());
    }

    @After
    public void tearDown() {
        contactService.deleteAll();
    }

}
