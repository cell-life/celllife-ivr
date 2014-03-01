package org.celllife.ivr.framework.campaign;

import org.apache.commons.collections.IteratorUtils;
import org.celllife.ivr.application.VerboiceApplicationService;
import org.celllife.ivr.domain.Campaign;
import org.celllife.ivr.domain.CampaignRepository;
import org.celllife.ivr.domain.Contact;
import org.celllife.ivr.domain.ContactRepository;
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
    private CampaignRepository campaignRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private VerboiceApplicationService verboiceApplicationService;

    public void sendMessagesForCampaign(Long campaignId, Long userId, Integer msgSlot, Date msgTime) {

        Campaign campaign = null;

        try {
            campaign = campaignRepository.findOne(campaignId);

            switch (campaign.getType()) {
                case DAILY:
                    processDailyCampaign(campaign, campaignId, msgSlot, msgTime);
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

    private void processDailyCampaign(Campaign campaign, Long campaignId, Integer msgSlot, Date msgTime) throws Exception {

        List<Contact> campaignContacts = IteratorUtils.toList(contactRepository.findContactsInCampaign(campaignId).iterator());

        int totalContacts = campaignContacts.size();
        log.info("sending messages for relative campaign: [id={}], [msgSlot={}], [msgTime={}], [totalContacts={}]",
                new Object[]{campaignId, msgSlot, msgTime, totalContacts});

        for (Contact campaignContact : campaignContacts) {

            verboiceApplicationService.enqueueCallForMsisdn(campaign.getChannelName(), campaign.getCallFlowName(), campaign.getScheduleName(), campaignContact.getMsisdn(), campaignContact.getPassword(), campaignContact.getProgress() + 1);
            campaignContact.setProgress(campaignContact.getProgress() + 1);
            contactRepository.save(campaignContact);

        }
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
