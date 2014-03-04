package org.celllife.ivr.framework.campaign;

import org.celllife.ivr.application.CampaignMessageService;
import org.celllife.ivr.application.CampaignService;
import org.celllife.ivr.application.ContactService;
import org.celllife.ivr.application.VerboiceApplicationService;
import org.celllife.ivr.domain.*;
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

    public void sendMessagesForCampaign(Long campaignId, Long userId, Integer msgSlot, Date msgTime) {

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
                //campaignRepository.updateCampaignStatus(campaign.getId(), CampaignStatus.SCHEDULE_ERROR); FIXME!
            }
        }
    }

    private void processDailyCampaign(Long campaignId, Integer messageSlot, Date messageTime) throws Exception {

        Campaign campaign = campaignService.getCampaign(campaignId);

        List<CampaignMessage> campaignMessages = campaignMessageService.findMessagesForTimeSlot(campaignId, messageTime, messageSlot);
        List<Contact> campaignContacts = contactService.getAllContacts();
        int totalContacts = 0;

        for (Contact campaignContact : campaignContacts) {
            for (CampaignMessage campaignMessage : campaignMessages) {
                if ((campaignContact.getProgress() == 0) && (campaignMessage.getMessageDay() == 1)) {
                    verboiceApplicationService.enqueueCallForMsisdn(campaign.getChannelName(), campaign.getCallFlowName(), campaign.getScheduleName(), campaignContact.getMsisdn(), campaignContact.getPassword(), campaignContact.getProgress() + 1);
                    campaignContact.setProgress(campaignContact.getProgress() + 1);
                    contactService.saveContact(campaignContact);
                    totalContacts++;
                } else if (campaignContact.getProgress()-1 > campaignMessage.getMessageDay()) {
                    verboiceApplicationService.enqueueCallForMsisdn(campaign.getChannelName(), campaign.getCallFlowName(), campaign.getScheduleName(), campaignContact.getMsisdn(), campaignContact.getPassword(), campaignContact.getProgress() + 1);
                    campaignContact.setProgress(campaignContact.getProgress() + 1);
                    contactService.saveContact(campaignContact);
                    totalContacts++;
                }
            }
        }

        log.info("sending messages for relative campaign: [id={}], [msgSlot={}], [msgTime={}], [totalContacts={}]",
                new Object[]{campaignId, messageSlot, messageTime, totalContacts});
    }

    /*private void processFlexiCampaign(Campaign campaign, Date msgTime) throws Exception {

		List<CampaignMessage> campaignMessages = campaignRepository.getCampMsgForFlexiCampaign(campaign, msgTime);

		log.info("sending messages for generic campaign: [id={}], [msgTime={}]", campaign.getId(), msgTime);

		for (CampaignMessage campaignMessage : campaignMessages) {
			int msgDay = campaignMessage.getMsgDay();
			List<Contact> campaignContacts = campaignRepository.getContactsToProcessForFlexiCampaign(campaign, msgDay, new Date());
			log.debug("Num contacts for [msgDay={}], [msgTime={}], [num={}], [campaignId={}]",
					new Object[] { msgDay, msgTime, campaignContacts.size(), campaign.getId() });

			for (Contact campaignContact : campaignContacts) {
                verboiceApplicationService.enqueueCallForMsisdn(campaign.getChannelName(),campaign.getCallFlowName(),campaign.getScheduleName(), campaignContact.getMsisdn(), campaignContact.getPassword(), campaignMessage.getMessageNumber() );
            }

		}

    }  */

}
