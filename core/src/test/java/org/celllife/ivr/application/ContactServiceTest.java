package org.celllife.ivr.application;

import junit.framework.Assert;
import org.celllife.ivr.application.campaign.CampaignService;
import org.celllife.ivr.application.contact.ContactService;
import org.celllife.ivr.application.jobs.RelativeCampaignJob;
import org.celllife.ivr.application.message.CampaignMessageService;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.campaign.CampaignType;
import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.CampaignNameExistsException;
import org.celllife.ivr.domain.exception.ContactExistsException;
import org.celllife.ivr.domain.exception.InvalidMsisdnException;
import org.celllife.ivr.domain.message.CampaignMessage;
import org.celllife.ivr.test.TestConfiguration;
import org.dozer.util.IteratorUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class ContactServiceTest extends TestConfiguration {

    @Autowired
    ContactService contactService;

    @Autowired
    CampaignService campaignService;

    @Autowired
    RelativeCampaignJob relativeCampaignJob;

    @Autowired
    CampaignMessageService campaignMessageService;

    @Before
    public void setUp() throws ContactExistsException, Exception {
        contactService.deleteAll();
        List<Contact> contactList = new ArrayList<Contact>();
        contactList.add(new Contact("27724194158","1234", 1L, 0));
        contactService.saveContacts(contactList);
        campaignService.deleteAllCampaigns();
    }

    @Test
    public void testGetAllContacts() {
        List<Contact> contactList = IteratorUtils.toList(contactService.getAllContacts().iterator());
        Assert.assertEquals(1,contactList.size());
    }

    @Test
    public void testFindNonVoidedContactsInCampaign() throws InvalidMsisdnException, ContactExistsException, CampaignNameExistsException {

        Campaign campaign = new Campaign("test", "test campaign", 3, "","","",1L);
        campaign = campaignService.saveCampaign(campaign);

        Contact contact = new Contact("27724194158", "1234", campaign.getId(), 0);
        contact = contactService.saveContact(contact);

        contact = new Contact("27724194160", "1234", campaign.getId(), 0);
        contact = contactService.saveContact(contact);

        contact = new Contact("27724194159", "1234", campaign.getId(), 0);
        contact.setVoided(true);
        contact = contactService.saveContact(contact);

        List<Contact> contacts = contactService.findNonVoidedContactsInCampaign(campaign.getId());

        Assert.assertEquals(2,contacts.size());

    }

    @After
    public void tearDown() {
        contactService.deleteAll();
    }

}
