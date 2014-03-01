package org.celllife.ivr.application;

import org.celllife.ivr.domain.Campaign;

import java.util.Date;
import java.util.List;

public interface CampaignService {

    void addMessagesToCampaign(Campaign campaign, List<Integer> verboiceMessageNumbers, List<Date> messageTimesOfDay) throws Exception;

    Campaign getCampaign(Long id);

    void createQuartzTriggerForCampaign(Long campaignId, Date msgTime, Integer msgSlot) throws Exception;

}
