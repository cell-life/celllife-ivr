package org.celllife.ivr.application.jobs;

import junit.framework.Assert;
import org.celllife.ivr.application.campaign.CampaignService;
import org.celllife.ivr.application.contact.ContactService;
import org.celllife.ivr.application.message.CampaignMessageService;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.message.CampaignMessage;
import org.celllife.ivr.domain.message.CampaignMessageDto;
import org.celllife.ivr.test.TestConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class RelativeCampaignJobTest extends TestConfiguration{

    @Autowired
    CampaignService campaignService;

    @Autowired
    ContactService contactService;

    @Autowired
    RelativeCampaignJob relativeCampaignJob;

    @Autowired
    CampaignMessageService campaignMessageService;

    @Before
    public void setUp() throws Exception {

        campaignService.deleteAllCampaigns();

    }

    @Test
    public void testGetMessageForContactInDailyCampaign() throws Exception {

        // Create and save campaign
        Campaign campaign = new Campaign("test", "test campaign", 3, "","","",1L);
        campaign = campaignService.saveCampaign(campaign);

        // Create and save messages
        List<Integer> verboiceMessageNumbers = new ArrayList<>();
        verboiceMessageNumbers.add(1);
        verboiceMessageNumbers.add(2);
        verboiceMessageNumbers.add(3);
        List<Date> timesOfMessages = new ArrayList<>();
        Date timeOfMessage = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 9);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        timeOfMessage = cal.getTime();
        timesOfMessages.add(timeOfMessage);
        timesOfMessages.add(timeOfMessage);
        timesOfMessages.add(timeOfMessage);
        campaignService.setMessagesForDailyCampaign(campaign.getId(), verboiceMessageNumbers, timesOfMessages);

        Contact contact = new Contact("27724194158", "1234", campaign.getId(), 0);
        contact = contactService.saveContact(contact);
        CampaignMessage campaignMessages = relativeCampaignJob.getMessageForContact(contact,campaignMessageService.findMessagesInCampaign(campaign.getId()));
        Assert.assertEquals(campaignMessages.getVerboiceMessageNumber(), 1);

        contact.setProgress(1);
        contact = contactService.saveContact(contact);
        campaignMessages = relativeCampaignJob.getMessageForContact(contact,campaignMessageService.findMessagesInCampaign(campaign.getId()));
        Assert.assertEquals(campaignMessages.getVerboiceMessageNumber(), 2);

        contact.setProgress(2);
        contact = contactService.saveContact(contact);
        campaignMessages = relativeCampaignJob.getMessageForContact(contact,campaignMessageService.findMessagesInCampaign(campaign.getId()));
        Assert.assertEquals(campaignMessages.getVerboiceMessageNumber(), 3);
    }

    @Test
    public void testGetMessageForContact() throws Exception {

        // Create and save campaign
        Campaign campaign = new Campaign("test 2", "test campaign 2", 3, "","","",1L);
        campaign = campaignService.saveCampaign(campaign);

        // Create and save messages
        List<Integer> verboiceMessageNumbers = new ArrayList<>();
        verboiceMessageNumbers.add(1);
        verboiceMessageNumbers.add(2);
        verboiceMessageNumbers.add(3);

        List<CampaignMessageDto> campaignMessageDtos = new ArrayList<>();

        CampaignMessageDto campaignMessageDto = new CampaignMessageDto();
        campaignMessageDto.setMessageDay(1);
        campaignMessageDto.setVerboiceMessageNumber(1);
        campaignMessageDto.setMessageTimeOfDay("9:00");
        campaignMessageDtos.add(campaignMessageDto);

        campaignMessageDto = new CampaignMessageDto();
        campaignMessageDto.setMessageDay(1);
        campaignMessageDto.setVerboiceMessageNumber(2);
        campaignMessageDto.setMessageTimeOfDay("14:00");
        campaignMessageDtos.add(campaignMessageDto);

        campaignMessageDto = new CampaignMessageDto();
        campaignMessageDto.setMessageDay(2);
        campaignMessageDto.setVerboiceMessageNumber(3);
        campaignMessageDto.setMessageTimeOfDay("9:30");
        campaignMessageDtos.add(campaignMessageDto);

        campaignService.setMessagesForCampaign(campaign.getId(),campaignMessageDtos);

        Contact contact = new Contact("27724194159", "1234", campaign.getId(), 0);
        contact = contactService.saveContact(contact);

        CampaignMessage campaignMessages = relativeCampaignJob.getMessageForContact(contact,campaignMessageService.findMessagesInCampaign(campaign.getId()));
        Assert.assertEquals(campaignMessages.getVerboiceMessageNumber(), 1);

        contact.setProgress(1);
        contact = contactService.saveContact(contact);
        campaignMessages = relativeCampaignJob.getMessageForContact(contact,campaignMessageService.findMessagesInCampaign(campaign.getId()));
        Assert.assertEquals(campaignMessages.getVerboiceMessageNumber(), 2);

        contact.setProgress(2);
        contact = contactService.saveContact(contact);
        campaignMessages = relativeCampaignJob.getMessageForContact(contact,campaignMessageService.findMessagesInCampaign(campaign.getId()));
        Assert.assertEquals(campaignMessages.getVerboiceMessageNumber(), 3);
    }

    @After
    public void tearDown() throws Exception {

        campaignService.deleteAllTriggers();
        campaignService.deleteAllCampaigns();

    }

}
