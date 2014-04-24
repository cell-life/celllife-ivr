package org.celllife.ivr.application.campaign;

import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.exception.CampaignNameExistsException;
import org.celllife.ivr.domain.exception.IvrException;
import org.celllife.ivr.domain.message.CampaignMessage;
import org.celllife.ivr.domain.message.CampaignMessageDto;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.util.Date;
import java.util.List;

public interface CampaignService {

    /**
     * Set the campaign messages for a campaign.
     * @param campaignId The Id of the campaign.
     * @param verboiceMessageNumbers A list of the message numbers in Verboice.
     * @param messageTimesOfDay A list of the message times.
     * @throws Exception
     */
    void setMessagesForDailyCampaign(Long campaignId, List<Integer> verboiceMessageNumbers, List<Date> messageTimesOfDay) throws Exception;

    /**
     * Get a campaign by id.
     * @param id The campaign id.
     * @return The campaign.
     */
    Campaign getCampaign(Long id);

    /**
     * Saves a campaign.
     * @param campaign The campaign to save.
     * @return The new/updated campaign.
     */
    Campaign saveCampaign(Campaign campaign) throws CampaignNameExistsException;

    /**
     * Gets the system scheduler.
     * @return The quartz scheduler.
     */
    Scheduler getScheduler();

    /**
     * Deletes all campaigns.
     */
    void deleteAllCampaigns();

    /**
     * Get all campaigns.
     * @return A list of campaigns.
     */
    List<Campaign> getAllCampaigns();

    void deleteTrigger(String triggerName, String groupName) throws SchedulerException;

    List<CampaignMessage> setMessagesForCampaign(Long campaignId, List<CampaignMessageDto> campaignMessageDtos) throws IvrException;

    public List<Campaign> findCampaignByName(String name);


}