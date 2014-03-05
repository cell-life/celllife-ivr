package org.celllife.ivr.application;

import org.celllife.ivr.application.utils.JsonUtils;
import org.celllife.ivr.domain.callog.CallLog;
import org.celllife.ivr.domain.callog.CallLogRepository;
import org.celllife.ivr.integration.verboice.VerboiceService;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class VerboiceApplicationServiceImpl implements VerboiceApplicationService {

    private static Logger log = LoggerFactory.getLogger(VerboiceApplicationServiceImpl.class);

    @Autowired
    VerboiceService verboiceService;

    @Autowired
    ContactService contactService;

    JsonUtils jsonUtils = new JsonUtils();

    @Autowired
    CallLogRepository callLogRepository;

    @Override
    public void enqueueCallForMsisdn(String channelName, String callFlowName, String scheduleName, String msisdn, String password, int messageNumber) throws Exception {

        String response = verboiceService.enqueueCall(channelName, callFlowName, scheduleName, msisdn, password, messageNumber);

        Map<String, String> responseVariables;

        try {
            responseVariables = jsonUtils.extractJsonVariables("{\"response\":" + response + "}");
        } catch (JSONException e) {
            log.warn("Unrecognized Response from Verboice Server. Response: " + response + "\n" + e.getStackTrace());
            throw new Exception("Unrecognized Response from Verboice Server.");
        }

        if (responseVariables.containsKey("call_id")){
            callLogRepository.save(new CallLog(new Date(), Long.parseLong(responseVariables.get("call_id")), msisdn,
                    password, 0, channelName, callFlowName, scheduleName, responseVariables.get("state"),messageNumber));
        } else {
            log.warn("No call ID returned from Verboice server.");
            throw new Exception("No call ID returned from Verboice server.");
        }

    }

}
