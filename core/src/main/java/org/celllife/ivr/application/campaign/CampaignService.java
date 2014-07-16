package org.celllife.ivr.application.campaign;

import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.exception.CampaignNameExistsException;
import org.celllife.ivr.domain.exception.IvrException;
import org.celllife.ivr.domain.message.CampaignMessage;
import org.celllife.ivr.domain.message.CampaignMessageDto;
import org.quartz.CronTrigger;
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
    Scheduler getScheduler(); //TODO: try and figure out how not to expose

    /**
     * Deletes all campaigns.
     */
    void deleteAllCampaigns();

    /**
     * Get all campaigns.
     * @return A list of campaigns.
     */
    List<Campaign> getAllCampaigns();

    /**
     * Deletes a quartz scheduler trigger by name and group.
     * @param triggerName The trigger name.
     * @param groupName The group name.
     * @throws SchedulerException
     */
    void deleteTrigger(String triggerName, String groupName) throws SchedulerException; //TODO: try and figure out how not to expose

    /**
     * Sets the messages for a particular campaign.
     * NB: Overrides any previous messages that were set. (Please don't hate me for this, I honestly thought it was the right thing to do.)
     * @param campaignId
     * @param campaignMessageDtos
     * @return The list of messages, as set in the campaign.
     * @throws IvrException
     */
    List<CampaignMessage> setMessagesForCampaign(Long campaignId, List<CampaignMessageDto> campaignMessageDtos) throws IvrException; //TODO: think of using replace instead

    /**
     * Returns all campaigns with a particular name.
     * @param name The campaign name.
     * @return A list of campaigns.
     */
    List<Campaign> findCampaignByName(String name);

    List<CronTrigger> findTriggerByJobNameAndGroup(String jobName, String jobGroup) throws SchedulerException; //TODO: try and figure out how not to expose


}