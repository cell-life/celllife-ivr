package org.celllife.ivr.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface CampaignMessageRepository extends PagingAndSortingRepository<CampaignMessage, String> {

    @Query("select new org.celllife.ivr.domain.CampaignMessage(cm.verboiceMessageNumber, cm.messageDay, cm.messageSlot, cm.messageTime) " +
    "from CampaignMessage cm " +
    "where cm.campaignId = :campaignId " +
    "and cm.messageSlot = :messageSlot " +
    "and cm.messageTime = :messageTime" )
    Iterable<CampaignMessage> findMessagesForTimeSlot(@Param("campaignId") Long campaignId, @Param("messageTime")
    Date messageTime, @Param("messageSlot") Integer messageSlot );

    @Query("select new org.celllife.ivr.domain.Contact(c.msisdn, c.password) " +
            "from Contact c " +
            "where c.campaignId = :campaignId" )
    Iterable<Contact> findContactsInCampaign(@Param("campaignId") Long campaignId);

}
