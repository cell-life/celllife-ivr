package org.celllife.ivr.application.jobs;

import org.celllife.ivr.application.calllog.CallLogService;
import org.celllife.ivr.application.campaign.CampaignService;
import org.celllife.ivr.application.utils.JsonUtils;
import org.celllife.ivr.application.verboice.VerboiceApplicationService;
import org.celllife.ivr.domain.callog.CallLog;
import org.celllife.ivr.domain.callog.CallStatus;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("backgroundServices")
public class BackgroundServiceImpl implements BackgroundService {

    private static Logger log = LoggerFactory.getLogger(BackgroundServiceImpl.class);

    @Autowired
    CallLogService callLogService;

    @Autowired
    VerboiceApplicationService verboiceApplicationService;

    @Autowired
    CampaignService campaignService;

    JsonUtils jsonUtils = new JsonUtils();

    @Override
    public void findFailedCallsAndRetry() {

        log.debug("Checking for failed calls to retry.");

        List<CallLog> callsToRetry = findCallsToRetry();

        for (CallLog callLog : callsToRetry) {

            log.debug("Retrying call with id " + callLog.getId() + ". Number of previous attempts: " + callLog.getAttempt());

            Integer attempt = 0;
            if (callLog.getAttempt() == null) {
                attempt = 2;
                retryCall(callLog, attempt);
            } else if (callLog.getAttempt() < 3) { //TODO: make this configurable
                attempt = callLog.getAttempt() + 1;
                retryCall(callLog, attempt);
            } else {
                log.debug("Call with id " + callLog.getId() + " has already been retried twice, and won't be retried again. ");
            }

        }

    }

    protected void retryCall(CallLog callLog, Integer attempt) {

        String response;
        try {
            response = verboiceApplicationService.enqueueCallForMsisdn(callLog.getChannelName(), callLog.getCallFlowName(), callLog.getScheduleName(), callLog.getMsisdn(), callLog.getMessageNumber(), callLog.getPassword());
        } catch (Exception e) {
            response = null;
            log.error("Error enqueuing call to Verboice. Call will be logged as 'waiting'.", e);
        }

        logCall(response, callLog.getMsisdn(), callLog.getPassword(), callLog.getMessageNumber(), callLog.getChannelName(), callLog.getCallFlowName(), callLog.getScheduleName(), attempt, callLog.getVerboiceProjectId());

        callLog.setRetryDone(true);
        callLogService.saveCallLog(callLog);

    }

    protected List<CallLog> findCallsToRetry() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date midnightToday = cal.getTime();

        //TODO: can make these states configurable
        List<CallLog> callLogListBusy = callLogService.findByStateAndRetryDoneAndDateGreaterThan(CallStatus.BUSY, false, midnightToday);
        List<CallLog> callLogListNoAnswer = callLogService.findByStateAndRetryDoneAndDateGreaterThan(CallStatus.NO_ANSWER, false, midnightToday);

        List<CallLog> callsToRetry = callLogListBusy;
        callsToRetry.addAll(callLogListNoAnswer);

        return callsToRetry;

    }

    protected void logCall(String response, String msisdn, String password, Integer messageNumber, String channelName, String callFlowName, String scheduleName, Integer attempt, Integer verboiceProjectId) {

        //if no response from Verboice, log the call as "waiting"
        if (response == null) {
            log.warn("No response from Verboice server.");
            CallLog callLog = new CallLog(new Date(), null, msisdn,
                    channelName, callFlowName, scheduleName, "waiting", messageNumber, password, verboiceProjectId, attempt, false);
            callLogService.saveCallLog(callLog);
            return;
        }

        Map<String, String> responseVariables = new HashMap<String,String>();
        try {
            responseVariables = jsonUtils.extractJsonVariables("{\"response\":" + response + "}");
        } catch (JSONException e) {
            log.warn("Unrecognized Response from Verboice Server. Response: " + response, e.getMessage());
        }

        // log the call with response variables from Verboice
        if (responseVariables.containsKey("call_id")) {
            CallLog callLog = new CallLog(new Date(), Long.parseLong(responseVariables.get("call_id")), msisdn,
                    channelName, callFlowName, scheduleName, responseVariables.get("state"), messageNumber, password, verboiceProjectId, attempt, false);
            callLogService.saveCallLog(callLog);
        }
        //if no response from Verboice, log the call as "waiting"
        else {
            log.warn("No call ID returned from Verboice server.");
            CallLog callLog = new CallLog(new Date(), null, msisdn, channelName, callFlowName, scheduleName, "waiting", messageNumber, password, verboiceProjectId, attempt, false);
            callLogService.saveCallLog(callLog);
        }

    }
}
