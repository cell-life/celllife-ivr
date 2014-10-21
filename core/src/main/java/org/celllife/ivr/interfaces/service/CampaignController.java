package org.celllife.ivr.interfaces.service;

import org.celllife.ivr.application.campaign.CampaignService;
import org.celllife.ivr.application.message.CampaignMessageService;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.campaign.CampaignDto;
import org.celllife.ivr.domain.campaign.CampaignStatus;
import org.celllife.ivr.domain.exception.CampaignNameExistsException;
import org.celllife.ivr.domain.exception.IvrException;
import org.celllife.ivr.domain.message.CampaignMessage;
import org.celllife.ivr.domain.message.CampaignMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    public static final int SC_UNPROCESSABLE_ENTITY = 422;

    @Autowired
    CampaignService campaignService;

    @Autowired
    CampaignMessageService campaignMessageService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value= "/service/campaigns", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<CampaignDto> createCampaign(@RequestBody List<CampaignDto> campaignDtos, HttpServletResponse response) {

        List<CampaignDto> campaignDtoList = new ArrayList<>(); //FIXME: this should probably return a single campaignDto, forgive me :(

        CampaignDto campaignDto = campaignDtos.get(0);

        if (campaignDtos.size() > 1) {
            log.warn("It is not possible to add more than one campaign at a time.");
            response.setStatus(SC_UNPROCESSABLE_ENTITY);
            return campaignDtoList;
        }

        Campaign campaign = new Campaign(campaignDto.getName(), campaignDto.getDescription(), campaignDto.getDuration(), campaignDto.getCallFlowName(), campaignDto.getChannelName(), campaignDto.getScheduleName(), campaignDto.getVerboiceProjectId());
        campaign.setStatus(CampaignStatus.ACTIVE);
        campaign.setStartDate(new Date());

        try {
            campaign = campaignService.saveCampaign(campaign);
            campaignDtoList.add(campaign.getCampaignDto());
            response.setStatus(HttpServletResponse.SC_CREATED);
            return campaignDtoList;
        } catch (CampaignNameExistsException e) {
            log.warn("Could not save campaign with name " + campaignDto.getName() + ". ", e);
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return campaignDtoList;
        }

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, value= "/service/campaigns/{campaignId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CampaignDto updateCampaign(@RequestBody Collection<CampaignDto> campaignDtos, @PathVariable Long campaignId, HttpServletResponse response) {

        CampaignDto campaignDto = campaignDtos.iterator().next();

        Campaign campaign = campaignService.getCampaign(campaignId);

        if (campaign == null) {
            log.warn("A campaign with id " + campaignId + " doesn't exist.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new CampaignDto();
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

        try {
            campaign = campaignService.saveCampaign(campaign);
        } catch (CampaignNameExistsException e) {
            log.warn("Could not save campaign.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new CampaignDto();
        }

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
    public CampaignDto getCampaign(@PathVariable Long campaignId, HttpServletResponse response) {

        Campaign campaign = campaignService.getCampaign(campaignId);

        if (campaign == null) {
            log.warn("A campaign with id " + campaignId + " doesn't exist.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new CampaignDto();
        }

        return campaign.getCampaignDto();

    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/service/campaigns/{campaignId}/campaignMessages")
    public Collection<CampaignMessageDto> setMessagesForCampaign(@RequestBody List<CampaignMessageDto> campaignMessages, @PathVariable Long campaignId, HttpServletResponse response) {

        Collection<CampaignMessageDto> campaignMessageDtos = new ArrayList<>();

        for(CampaignMessageDto campaignMessage : campaignMessages){
            DateFormat formatter = new SimpleDateFormat("HH:mm");
            try {
                Date date = (Date)formatter.parse(campaignMessage.getMessageTimeOfDay());
            } catch (ParseException e) {
                log.warn("Message times must be in the format HH:mm.", e);
                response.setStatus(SC_UNPROCESSABLE_ENTITY);
                return campaignMessageDtos;
            }
        }

        List<CampaignMessage> campaignMessagesReturned = null;
        try {
            campaignMessagesReturned = campaignService.setMessagesForCampaign(campaignId,campaignMessages);
        } catch (IvrException e) {
            log.warn("Could not set campaign messages.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return campaignMessageDtos;
        }

        for (CampaignMessage campaignMessage : campaignMessagesReturned) {
            campaignMessageDtos.add(campaignMessage.getCampaignMessageDto());
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
        return campaignMessageDtos;

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
