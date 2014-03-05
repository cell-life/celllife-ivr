package org.celllife.ivr.application;

import org.celllife.ivr.domain.campaign.Campaign;
import org.quartz.Scheduler;

import java.util.Date;
import java.util.List;

public interface CampaignService {

    void setMessagesForCampaign(Long campaignId, List<Integer> verboiceMessageNumbers, List<Date> messageTimesOfDay) throws Exception;

    Campaign getCampaign(Long id);

    Campaign saveCampaign(Campaign campaign);

    Scheduler getScheduler();

    void deleteAllCampaigns();

    List<Campaign> findAllCampaigns();

}
