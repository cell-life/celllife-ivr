package org.celllife.ivr.integration.verboice;

import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.IvrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class VerboiceServiceImpl implements VerboiceService {

    private static Logger log = LoggerFactory.getLogger(VerboiceServiceImpl.class);

    @Autowired
    VerboiceClient verboiceClient;

    @Value("${verboice.base_url}")
    private String verboiceBaseUrl;

    /**
     * Enqueues call to Verboice
     * @param channelName
     * @param callFlowName
     * @param scheduleName
     * @param msisdn
     * @return string of format {"call_id":58,"state":"queued"}
     * @throws Exception
     */
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

}
