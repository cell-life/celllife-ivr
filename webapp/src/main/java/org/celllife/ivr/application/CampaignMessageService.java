package org.celllife.ivr.application;

import org.celllife.ivr.domain.CampaignMessage;

import java.util.Date;
import java.util.List;

public interface CampaignMessageService {

   void save(CampaignMessage campaignMessage);

   List<CampaignMessage> findMessagesForTimeSlot(Long campaignId, Date messageTime, Integer messageSlot);

}
