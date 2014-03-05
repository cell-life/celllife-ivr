package org.celllife.ivr.application.jobs;

import org.celllife.ivr.application.CampaignMessageService;
import org.celllife.ivr.application.CampaignService;
import org.celllife.ivr.application.ContactService;
import org.celllife.ivr.application.VerboiceApplicationService;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.campaign.CampaignStatus;
import org.celllife.ivr.domain.contact.Contact;
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
            switch (campaign.getType()) {
                case DAILY:
                    processDailyCampaign(campaignId, msgSlot, msgTime);
                    break;
                case FLEXI:
                    //processFlexiCampaign(campaign, msgTime);  FIXME: need to implement this
                    break;
                default:
                    throw new Exception("Trying to run fixed campaign with relative campaign job");
            }
        } catch (Exception e) {
            log.error("Error running relative campaign job. [campaign=" + campaignId + "]", e);
            if (campaign != null) {
                campaign.setStatus(CampaignStatus.SCHEDULE_ERROR);
                campaignService.saveCampaign(campaign);
            }
        }
    }

    private void processDailyCampaign(Long campaignId, Integer messageSlot, Date messageTime) throws Exception {

        Campaign campaign = campaignService.getCampaign(campaignId);

        List<CampaignMessage> campaignMessages = campaignMessageService.findMessagesForTimeSlot(campaignId, messageTime, messageSlot);
        List<Contact> campaignContacts = contactService.getAllContacts();

        for (Contact campaignContact : campaignContacts) {

            CampaignMessage campaignMessage = getMessageForContact(campaignContact, campaignMessages);

            if (campaignMessage != null) {
                verboiceApplicationService.enqueueCallForMsisdn(campaign.getChannelName(), campaign.getCallFlowName(), campaign.getScheduleName(), campaignContact.getMsisdn(), campaignContact.getPassword(), campaignMessage.getVerboiceMessageNumber());
                campaignContact.setProgress(campaignMessage.getVerboiceMessageNumber());
                contactService.saveContact(campaignContact);
            }

        }

        log.info("sending messages for relative campaign: [id={}], [msgSlot={}], [msgTime={}]]",
                new Object[]{campaignId, messageSlot, messageTime});
    }

    protected CampaignMessage getMessageForContact(Contact campaignContact, List<CampaignMessage> campaignMessages) {

        for (CampaignMessage campaignMessage : campaignMessages) {
            if ((campaignContact.getProgress()+1) ==  campaignMessage.getMessageDay()) {
                return campaignMessage;
            }
        }

        return null;
    }

}
