package org.celllife.ivr.integration.verboice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.celllife.ivr.domain.exception.IvrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public final class VerboiceClient {
	
	private static Logger log = LoggerFactory.getLogger(VerboiceClient.class);

    @Autowired
    private HttpClient verboiceHttpClient;

    @Autowired
    private Header verboiceAuthenticationHeader;

    @Autowired
    private ObjectMapper objectMapper;

    public String get(String url) throws IvrException {

        HttpGet method = newHttpGetMethod(url);

        HttpResponse response = execute(method);
        if (response == null) {
            throw new IvrException("No response from server at " + url);
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IvrException(response.getStatusLine().toString());
        }

        HttpEntity responseEntity = response.getEntity();
        if (responseEntity == null) {
            return null;
        }

        return toString(responseEntity);

    }

    private HttpGet newHttpGetMethod(String url) {
        HttpGet method = new HttpGet(url);
        method.addHeader(verboiceAuthenticationHeader);
        method.addHeader("Accept", "application/json");

        HttpConnectionParams.setConnectionTimeout(method.getParams(), 10000);
        HttpConnectionParams.setSoTimeout(method.getParams(), 10000);

        return method;
    }

    private String toString(HttpEntity responseEntity)  {
        try {
            return EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpResponse execute(HttpGet method)  {
        try {
            return verboiceHttpClient.execute(method, (HttpContext) null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
