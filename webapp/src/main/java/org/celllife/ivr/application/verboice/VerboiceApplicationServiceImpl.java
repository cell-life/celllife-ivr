package org.celllife.ivr.application.verboice;

import org.celllife.ivr.application.campaign.CampaignService;
import org.celllife.ivr.application.utils.JsonUtils;
import org.celllife.ivr.domain.callog.CallLog;
import org.celllife.ivr.domain.callog.CallLogRepository;
import org.celllife.ivr.domain.exception.IvrException;
import org.celllife.ivr.integration.verboice.VerboiceService;
import org.json.JSONException;
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

    @Autowired
    CallLogRepository callLogRepository;

    @Override
    public void enqueueCallForMsisdn(String channelName, String callFlowName, String scheduleName, String msisdn, int messageNumber, String password)  {

        String response = "";

        try {
            response = verboiceService.enqueueCallWithPassword(channelName, callFlowName, scheduleName, msisdn, messageNumber, password);
        } catch (IvrException e) {
            log.warn("Could not enqueue call to verboice. Reason: " +e.getMessage());
        }

        Map<String, String> responseVariables = new HashMap<String,String>();

        try {
            responseVariables = jsonUtils.extractJsonVariables("{\"response\":" + response + "}");
        } catch (JSONException e) {
            log.warn("Unrecognized Response from Verboice Server. Response: " + response, e.getMessage());
        }

        if (responseVariables.containsKey("call_id")) {
            callLogRepository.save(new CallLog(new Date(), Long.parseLong(responseVariables.get("call_id")), msisdn,
                    0, channelName, callFlowName, scheduleName, responseVariables.get("state"), messageNumber));
        } else {
            log.warn("No call ID returned from Verboice server.");
            callLogRepository.save(new CallLog(new Date(), null, msisdn,
                    0, channelName, callFlowName, scheduleName, "ERROR", messageNumber));
        }

    }

}
