package org.celllife.ivr.application.verboice;

import org.celllife.ivr.application.campaign.CampaignService;
import org.celllife.ivr.application.utils.JsonUtils;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.exception.IvrException;
import org.celllife.ivr.integration.verboice.VerboiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional("transactionManager")
public class VerboiceApplicationServiceImpl implements VerboiceApplicationService {

    private static Logger log = LoggerFactory.getLogger(VerboiceApplicationServiceImpl.class);

    @Autowired
    VerboiceService verboiceService;

    @Autowired
    CampaignService campaignService;

    JsonUtils jsonUtils = new JsonUtils();

    @Override
    public String enqueueCallForMsisdn(Campaign campaign, String msisdn, int messageNumber, String password)  {

        String response = "";

        try {
            response = verboiceService.enqueueCallWithPassword(campaign.getChannelName(), campaign.getCallFlowName(), campaign.getScheduleName(), msisdn, messageNumber, password);
        } catch (IvrException e) {
            log.warn("Could not enqueue call to verboice. Reason: " +e.getMessage());
        }

        return response;

    }

    @Override
    public String enqueueCallForMsisdn(String channelName, String callFlowName, String scheduleName, String msisdn, int messageNumber, String password)  {

        String response = "";

        try {
            response = verboiceService.enqueueCallWithPassword(channelName, callFlowName, scheduleName, msisdn, messageNumber, password);
        } catch (IvrException e) {
            log.warn("Could not enqueue call to verboice. Reason: " +e.getMessage());
        }

        return response;

    }

}
