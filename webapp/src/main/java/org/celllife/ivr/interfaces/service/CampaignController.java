package org.celllife.ivr.interfaces.service;

import org.celllife.ivr.application.campaign.CampaignService;
import org.celllife.ivr.application.message.CampaignMessageService;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.campaign.CampaignDto;
import org.celllife.ivr.domain.campaign.CampaignStatus;
import org.celllife.ivr.domain.campaign.CampaignType;
import org.celllife.ivr.domain.exception.IvrException;
import org.celllife.ivr.domain.message.CampaignMessage;
import org.celllife.ivr.domain.message.CampaignMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    CampaignMessageService campaignMessageService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value= "/service/campaigns", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<CampaignDto> createCampaign(@RequestBody List<CampaignDto> campaignDtos, HttpServletResponse response) throws IvrException{

        CampaignDto campaignDto = campaignDtos.get(0);

        if (campaignDtos.size() > 1) {
            throw new IvrException("You can only add one campaign at a time!");
        }

        Campaign campaign = new Campaign(campaignDto.getName(), CampaignType.DAILY, campaignDto.getDescription(), campaignDto.getTimesPerDay(), campaignDto.getDuration(), campaignDto.getCallFlowName(), campaignDto.getChannelName(), campaignDto.getScheduleName(), campaignDto.getVerboiceProjectId());
        campaign.setStatus(CampaignStatus.ACTIVE);
        campaign.setStartDate(new Date());
        campaign = campaignService.saveCampaign(campaign);

        List<CampaignDto> campaignDtoList = new ArrayList<>();
        campaignDtoList.add(campaign.getCampaignDto());

        response.setStatus(HttpServletResponse.SC_CREATED);
        return campaignDtoList;

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, value= "/service/campaigns/{campaignId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CampaignDto updateCampaign(@RequestBody Collection<CampaignDto> campaignDtos, @PathVariable Long campaignId) throws IvrException{

        CampaignDto campaignDto = campaignDtos.iterator().next();

        Campaign campaign = campaignService.getCampaign(campaignId);

        if (campaign == null) {
            throw new IvrException("A campaign with this ID doesn't exist!");
        }

        if (campaignDto.getScheduleName() != null)
            campaign.setScheduleName(campaignDto.getScheduleName());
        if (campaignDto.getChannelName() != null)
            campaign.setChannelName(campaignDto.getChannelName());
        if (campaignDto.getCallFlowName() != null)
            campaign.setCallFlowName(campaignDto.getCallFlowName());
        if (campaignDto.getDescription() != null)
            campaign.setDescription(campaignDto.getDescription());
        if (campaignDto.getDuration() != null)
            campaign.setDuration(campaignDto.getDuration());
        if (campaignDto.getName() != null)
            campaign.setName(campaignDto.getName());
        if (campaignDto.getTimesPerDay() != null)
            campaign.setTimesPerDay(campaignDto.getTimesPerDay());

        campaign = campaignService.saveCampaign(campaign);

        return campaign.getCampaignDto();

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
    @RequestMapping(value = "/service/campaigns/{campaignId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CampaignDto getCampaign(@PathVariable Long campaignId) throws IvrException {

        Campaign campaign = campaignService.getCampaign(campaignId);

        if (campaign == null) {
            throw new IvrException("A campaign with this ID doesn't exist!");
        }

        return campaign.getCampaignDto();

    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/service/campaigns/{campaignId}/campaignMessages")
    public ResponseEntity<String> setMessagesForCampaign(@RequestBody List<CampaignMessageDto> campaignMessages, @PathVariable Long campaignId) {

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
            campaignService.setMessagesForCampaign(campaignId, verboiceMessageNumbers, messageTimesOfDay);
        } catch (Exception e) {
            log.warn("Error Adding Messages to Campaign. " + e.getMessage(), e);
            return new ResponseEntity<String>("Error Adding Messages to Campaign. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("Successfully added messages.", HttpStatus.OK);

    }

    @ResponseBody
    @RequestMapping(value = "/service/campaigns/{campaignId}/campaignMessages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<CampaignMessageDto> getCampaignMessages(@PathVariable Long campaignId) {

        List<CampaignMessage> campaignMessages = campaignMessageService.findMessagesInCampaign(campaignId);

        Collection<CampaignMessageDto> campaignMessageDtos = new ArrayList<>();

        for (CampaignMessage campaignMessage : campaignMessages) {
            campaignMessageDtos.add(campaignMessage.getCampaignMessageDto());
        }

        return campaignMessageDtos;

    }

}
