package org.celllife.ivr.domain.verboice;

import org.celllife.ivr.application.verboice.VerboiceApplicationService;
import org.celllife.ivr.domain.verboice.contacts.Contacts;
import org.celllife.ivr.test.TestConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:/META-INF/spring/spring-jdbc-verboice.xml",
        "classpath:/META-INF/spring/spring-orm-verboice.xml",
        "classpath:/META-INF/spring/spring-tx-verboice.xml",
        "classpath:/META-INF/spring-data/spring-data-jpa-verboice.xml",
        "classpath:/META-INF/spring-integration/spring-integration-verboice.xml"
})
public class ContactsRepositoryTest extends TestConfiguration {

    @Autowired
    VerboiceApplicationService verboiceApplicationService;

    @Ignore
    @Test
    public void testAddNewContact() {

        //Contacts verboiceContact = new Contacts(7, new Date(), new Date());

        //verboiceContact = verboiceApplicationService.saveVerboiceContact(verboiceContact);

    }



}
