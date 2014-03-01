package org.celllife.ivr.integration.verboice;

import org.celllife.ivr.domain.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class VerboiceServiceImpl implements VerboiceService {

    @Autowired
    VerboiceClient verboiceClient;

    @Value("${verboice.base_url}")
    private String verboiceBaseUrl;

    /**
     *
     * @param channelName
     * @param callFlowName
     * @param scheduleName
     * @param msisdn
     * @return string of format {"call_id":58,"state":"queued"}
     * @throws Exception
     */
    @Override
    public String enqueueCall(String channelName, String callFlowName, String scheduleName, String msisdn, String password, int messageNumber) throws Exception{

        String response = null;
        String url = verboiceBaseUrl;
        try {
            url = url.concat("api/call?");
            url = url.concat("channel=" + URLEncoder.encode(channelName, "UTF-8"));
            url = url.concat("&address=" + msisdn);
            url = url.concat("&call_flow=" + URLEncoder.encode(callFlowName, "UTF-8"));
            url = url.concat("&schedule=" + URLEncoder.encode(scheduleName, "UTF-8"));
            url = url.concat("&vars[password]=" + password);
            url = url.concat("&vars[message]=" + messageNumber);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        response = verboiceClient.get(url);

        return response;

    }

    /**
     *
     * @param channelName
     * @param callFlowName
     * @param scheduleName
     * @param contact
     * @param messageNumber
     * @return string of format {"call_id":58,"state":"queued"}
     * @throws Exception
     */
    @Override
    public String enqueueCall(String channelName, String callFlowName, String scheduleName, Contact contact, int messageNumber)  throws Exception {

        String response = null;
        String url = verboiceBaseUrl;
        try {
            url = url.concat("api/call?");
            url = url.concat("channel=" + URLEncoder.encode(channelName, "UTF-8"));
            url = url.concat("&address=" + contact.getMsisdn());
            url = url.concat("&call_flow=" + URLEncoder.encode(callFlowName, "UTF-8"));
            url = url.concat("&schedule=" + URLEncoder.encode(scheduleName, "UTF-8"));
            url = url.concat("&vars[password]=" + contact.getPassword());
            url = url.concat("&vars[message]=" + messageNumber);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        response = verboiceClient.get(url);

        return response;

    }


}
