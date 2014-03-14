package org.celllife.ivr.domain.verboice;

import org.celllife.ivr.domain.verboice.contact.Contacts;
import org.celllife.ivr.domain.verboice.contact.ContactsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:/META-INF/spring/spring-cache.xml",
        "classpath:/META-INF/spring/spring-config.xml",
        "classpath:/META-INF/spring/spring-domain-verboice.xml",
        "classpath:/META-INF/spring/spring-dozer.xml",
        "classpath:/META-INF/spring/spring-jdbc-verboice.xml",
        "classpath:/META-INF/spring/spring-json.xml",
        "classpath:/META-INF/spring/spring-orm-verboice.xml",
        "classpath:/META-INF/spring/spring-task.xml",
        "classpath:/META-INF/spring/spring-tx-verboice.xml",
        "classpath:/META-INF/spring-data/spring-data-jpa-verboice.xml",
        "classpath:/META-INF/spring-integration/spring-integration-verboice.xml"
})
@TransactionConfiguration(transactionManager = "transactionManagerVerboice")

public class ContactRepositoryTest {

    @Autowired
    ContactsRepository contactRepository;

    @Test
    public void testAddNewContact() {

        Contacts verboiceContact = new Contacts(7, new Date(), new Date());

        contactRepository.save(verboiceContact);

        //Iterable<Contacts> verboiceContacts = contactRepository.findAll();

        System.out.println("hello");

    }

}
