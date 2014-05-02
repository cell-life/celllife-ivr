package org.celllife.ivr.application.jobs;

import org.celllife.ivr.application.campaign.CampaignService;
import org.celllife.ivr.application.contact.ContactService;
import org.celllife.ivr.application.message.CampaignMessageService;
import org.celllife.ivr.application.verboice.VerboiceApplicationService;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.ContactExistsException;
import org.celllife.ivr.domain.message.CampaignMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("relativeCampaignJob")
public class RelativeCampaignJob {

    public static final String NAME = "relativeCampaignJob";

    private static final Logger log = LoggerFactory.getLogger(RelativeCampaignJob.class);

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private CampaignMessageService campaignMessageService;

    @Autowired
    private VerboiceApplicationService verboiceApplicationService;

    public void sendMessagesForCampaign(Long campaignId, Integer msgSlot, Date msgTime) {

        Campaign campaign = campaignService.getCampaign(campaignId);

        try {
            processCampaignMessagesToSend(campaignId, msgSlot, msgTime);
        } catch (Exception e) {
            log.error("Error running relative campaign job. [campaign=" + campaignId + "]", e);
        }
    }

    private void processCampaignMessagesToSend(Long campaignId, Integer messageSlot, Date messageTime) {

        Campaign campaign = campaignService.getCampaign(campaignId);

        List<CampaignMessage> campaignMessages = campaignMessageService.findMessagesForTimeSlot(campaignId, messageTime, messageSlot);
        List<Contact> campaignContacts = contactService.findNonVoidedContactsInCampaign(campaignId);

        log.info("sending messages for relative campaign: [id={}], [msgSlot={}], [msgTime={}]]",
                new Object[]{campaignId, messageSlot, messageTime});

        for (Contact campaignContact : campaignContacts) {

            CampaignMessage campaignMessage = getMessageForContact(campaignContact, campaignMessages);

            if (campaignMessage != null) {

                verboiceApplicationService.enqueueCallForMsisdn(campaign, campaignContact.getMsisdn(), campaignMessage.getVerboiceMessageNumber(), campaignContact.getPassword());
                campaignContact.setProgress(campaignMessage.getVerboiceMessageNumber());

                try {
                    contactService.saveContact(campaignContact);
                } catch (ContactExistsException e) {
                    log.warn("Error saving contact with id " + campaignContact.getId() + " Reason: " + e.getMessage());
                }
            }


        }

    }

    protected CampaignMessage getMessageForContact(Contact campaignContact, List<CampaignMessage> campaignMessages) {

        for (CampaignMessage campaignMessage : campaignMessages) {
            if ((campaignContact.getProgress() + 1) ==  campaignMessage.getSequenceNumber()) {
                return campaignMessage;
            }
        }

        return null;
    }

}
