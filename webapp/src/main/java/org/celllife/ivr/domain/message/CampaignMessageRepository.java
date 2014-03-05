package org.celllife.ivr.domain.message;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface CampaignMessageRepository extends PagingAndSortingRepository<CampaignMessage, Long> {

    @Query("select new org.celllife.ivr.domain.message.CampaignMessage(cm.id, cm.verboiceMessageNumber, cm.messageDay, cm.messageSlot, cm.messageTime, cm.campaignId) " +
    "from CampaignMessage cm " +
    "where cm.campaignId = :campaignId " +
    "and cm.messageSlot = :messageSlot " +
    "and cm.messageTime = :messageTime" )
    Iterable<CampaignMessage> findMessagesForTimeSlot(@Param("campaignId") Long campaignId, @Param("messageTime")
    Date messageTime, @Param("messageSlot") Integer messageSlot );

    @Query("select new org.celllife.ivr.domain.message.CampaignMessage(cm.id, cm.verboiceMessageNumber, cm.messageDay, cm.messageSlot, cm.messageTime, cm.campaignId) " +
            "from CampaignMessage cm " +
            "where cm.campaignId = :campaignId")
    Iterable<CampaignMessage> findMessagesForCampaign(@Param("campaignId") Long campaignId);

}
