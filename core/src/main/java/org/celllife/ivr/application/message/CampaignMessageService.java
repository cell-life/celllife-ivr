package org.celllife.ivr.application.message;

import org.celllife.ivr.domain.message.CampaignMessage;

import java.util.Date;
import java.util.List;

public interface CampaignMessageService {

    /**
     * Saves a campaign message.
     * @param campaignMessage
     */
   void save(CampaignMessage campaignMessage);

    /**
     * Find messages for a time slot in a campaign.
     * @param campaignId The campaign id.
     * @param messageTime The message time.
     * @param messageSlot The message slot.
     * @return a list of messages
     */
   List<CampaignMessage> findMessagesForTimeSlot(Long campaignId, Date messageTime, Integer messageSlot);

    /**
     * Find the messages in a campaign.
     * @param campaignId The campaign id.
     * @return A list of messages.
     */
   List<CampaignMessage> findMessagesInCampaign(Long campaignId);

    /**
     * Deletes a campaign message.
     * @param campaignMessageId Id of message to delete.
     */
    void deleteMessage(Long campaignMessageId);

}
