package org.celllife.ivr.interfaces.service;

import org.celllife.ivr.application.CampaignService;
import org.celllife.ivr.domain.Campaign;
import org.celllife.ivr.domain.CampaignDto;
import org.celllife.ivr.domain.CampaignMessageDto;
import org.celllife.ivr.domain.CampaignType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @Autowired
    CampaignService campaignService;

    //FIXME: name these urls properly

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value= "/service/campaign")
    public String createCampaign(@RequestBody List<CampaignDto> campaignDtos) {

        CampaignDto campaignDto = campaignDtos.get(0);

        Campaign campaign = new Campaign(campaignDto.getName(), CampaignType.DAILY, campaignDto.getDescription(), campaignDto.getTimesPerDay(), campaignDto.getDuration(), campaignDto.getCallFlowName(), campaignDto.getChannelName(), campaignDto.getScheduleName());

        campaign = campaignService.saveCampaign(campaign);

        return("Campaign created with id " + campaign.getId());

    }

    @ResponseBody
    @RequestMapping(value = "/service/campaigns", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<CampaignDto> getCampaigns() {
       List<Campaign> campaigns = campaignService.findAllCampaigns();

        Collection<CampaignDto> campaignDtos = new ArrayList<>();

        for (Campaign campaign : campaigns) {
            campaignDtos.add(new CampaignDto(campaign));
        }

        return campaignDtos;

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/service/campaign/{campaignId}/campaignMessages")
    public String addMessagesToCampaign(@RequestBody List<CampaignMessageDto> campaignMessages, @PathVariable Integer campaignId) throws Exception {

        List<Integer> verboiceMessageNumbers = new ArrayList<>();

        List<Date> messageTimesOfDay = new ArrayList<>();

        for(CampaignMessageDto campaignMessage : campaignMessages){

            verboiceMessageNumbers.add(campaignMessage.getVerboiceMessageNumber());

            DateFormat formatter = new SimpleDateFormat("hh:mm");
            try {
                Date date = (Date)formatter.parse(campaignMessage.getMessageTimeOfDay());
                messageTimesOfDay.add(date);
            } catch (ParseException e) {
               throw new Exception("Times must be in the format hh:mm");
            }

        }

        campaignService.setMessagesForCampaign(campaignId.longValue(), verboiceMessageNumbers, messageTimesOfDay);

        return ("Success");

    }

}
