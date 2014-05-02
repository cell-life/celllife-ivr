package org.celllife.ivr.integration.verboice;

import org.celllife.ivr.application.contact.ContactService;
import org.celllife.ivr.application.utils.JsonUtils;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.exception.IvrException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class VerboiceServiceImpl implements VerboiceService {

    private static Logger log = LoggerFactory.getLogger(VerboiceServiceImpl.class);

    @Autowired
    VerboiceClient verboiceClient;

    @Value("${verboice.base_url}")
    private String verboiceBaseUrl;

    @Autowired
    ContactService contactService;

    JsonUtils jsonUtils = new JsonUtils();

    @Override
    public String enqueueCall(String channelName, String callFlowName, String scheduleName, String msisdn, int messageNumber) throws Exception {

        String response = null;
        String url = verboiceBaseUrl;
        try {
            url = url.concat("api/call?");
            url = url.concat("channel=" + URLEncoder.encode(channelName, "UTF-8"));
            url = url.concat("&address=" + msisdn);
            url = url.concat("&call_flow=" + URLEncoder.encode(callFlowName, "UTF-8"));
            url = url.concat("&schedule=" + URLEncoder.encode(scheduleName, "UTF-8"));
            url = url.concat("&vars[message]=" + messageNumber);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        log.debug("Enqueueing call to Verboice. HTTP GET " + url);

        response = verboiceClient.get(url);

        return response;

    }

    @Override
    public String enqueueCallWithPassword(String channelName, String callFlowName, String scheduleName, String msisdn, int messageNumber, String password) throws IvrException{

        String response = null;
        String url = verboiceBaseUrl;
        try {
            url = url.concat("api/call?");
            url = url.concat("channel=" + URLEncoder.encode(channelName, "UTF-8"));
            url = url.concat("&address=" + msisdn);
            url = url.concat("&call_flow=" + URLEncoder.encode(callFlowName, "UTF-8"));
            url = url.concat("&schedule=" + URLEncoder.encode(scheduleName, "UTF-8"));
            url = url.concat("&vars[message]=" + messageNumber);
            url = url.concat("&vars[password]=" + password);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        log.debug("Enqueueing call to Verboice. HTTP GET " + url);

        response = verboiceClient.get(url);

        return response;

    }

    @Override
    public void getUpdatedContactDetails(String msisdn, Campaign campaign) throws IvrException {

        String url = verboiceBaseUrl;

        try {
            url = url.concat("api/projects/");
            url = url.concat(campaign.getVerboiceProjectId().toString());
            url = url.concat("/contacts/by_address/");
            url = url.concat(URLEncoder.encode(msisdn, "UTF-8"));
            url = url.concat(".json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        log.debug("Enqueueing call to Verboice. HTTP GET " + url);

        String response = verboiceClient.get(url);

        Map<String, String> responseVariables = new HashMap<>();
        try {
            responseVariables = jsonUtils.extractJsonVariables("{\"response\":" + response + "}");
        } catch (JSONException e) {
            log.warn("Could not extract JSON variables from Verboice response " + response);
        }

        Map<String, String> contactVariables = new HashMap<>();
        try {
            contactVariables = jsonUtils.extractJsonVariables("{\"response\":" + responseVariables.get("vars") + "}");
        } catch (JSONException e) {
            log.warn("Could not extract JSON variables from Verboice contact vars " + responseVariables.get("vars"));
        }

        //TODO: do something with returned data

    }

}
