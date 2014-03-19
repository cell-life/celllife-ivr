package org.celllife.ivr.interfaces.service;

import org.celllife.ivr.application.campaign.CampaignService;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.campaign.CampaignDto;
import org.celllife.ivr.domain.campaign.CampaignStatus;
import org.celllife.ivr.domain.campaign.CampaignType;
import org.celllife.ivr.domain.message.CampaignMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
public class CampaignController {

    private static Logger log = LoggerFactory.getLogger(CampaignController.class);

    @Autowired
    CampaignService campaignService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value= "/service/campaigns")
    public ResponseEntity<String> createCampaign(@RequestBody List<CampaignDto> campaignDtos){

        CampaignDto campaignDto = campaignDtos.get(0);

        if (campaignDtos.size() > 1) {
            return new ResponseEntity<String>("Unfortunately you can only add one campaign at a time.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Campaign campaign = new Campaign(campaignDto.getName(), CampaignType.DAILY, campaignDto.getDescription(), campaignDto.getTimesPerDay(), campaignDto.getDuration(), campaignDto.getCallFlowName(), campaignDto.getChannelName(), campaignDto.getScheduleName());
        campaign.setStatus(CampaignStatus.ACTIVE);
        campaign.setStartDate(new Date());
        campaign = campaignService.saveCampaign(campaign);

        return new ResponseEntity<String>("Campaign created with id " + campaign.getId(),HttpStatus.OK);

    }

    @ResponseBody
    @RequestMapping(value = "/service/campaigns", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<CampaignDto> getCampaigns() {

        List<Campaign> campaigns = campaignService.getAllCampaigns();

        Collection<CampaignDto> campaignDtos = new ArrayList<>();

        for (Campaign campaign : campaigns) {
            campaignDtos.add(campaign.getCampaignDto());
        }

        return campaignDtos;

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/service/campaigns/{campaignId}/campaignMessages")
    public ResponseEntity<String> addMessagesToCampaign(@RequestBody List<CampaignMessageDto> campaignMessages, @PathVariable Integer campaignId) {

        List<Integer> verboiceMessageNumbers = new ArrayList<>();

        List<Date> messageTimesOfDay = new ArrayList<>();

        for(CampaignMessageDto campaignMessage : campaignMessages){

            verboiceMessageNumbers.add(campaignMessage.getVerboiceMessageNumber());

            DateFormat formatter = new SimpleDateFormat("hh:mm");
            try {
                Date date = (Date)formatter.parse(campaignMessage.getMessageTimeOfDay());
                messageTimesOfDay.add(date);
            } catch (ParseException e) {
                log.warn("An error occurred. Message times must be in the format hh:mm.", e);
                return new ResponseEntity<String>("An error occurred. Message times must be in the format hh:mm.",HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }

        try {
            campaignService.setMessagesForCampaign(campaignId.longValue(), verboiceMessageNumbers, messageTimesOfDay);
        } catch (Exception e) {
            log.warn("Error Adding Messages to Campaign. " + e.getMessage(), e);
            return new ResponseEntity<String>("Error Adding Messages to Campaign. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("Successfully added messages.", HttpStatus.OK);

    }

}
