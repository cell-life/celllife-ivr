package org.celllife.ivr.integration.verboice;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.celllife.ivr.domain.exception.IvrException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public final class VerboiceClient {

    @Autowired
    private HttpClient verboiceHttpClient;

    @Autowired
    private Header verboiceAuthenticationHeader;


    public String get(String url) throws IvrException {

        HttpGet httpGet = newHttpGetMethod(url);

        HttpResponse response = execute(httpGet);
        if (response == null) {
            httpGet.releaseConnection();
            throw new IvrException("No response from server at " + url);
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            httpGet.releaseConnection();
            throw new IvrException(response.getStatusLine().toString());
        }

        HttpEntity responseEntity = response.getEntity();
        if (responseEntity == null) {
            httpGet.releaseConnection();
            return null;
        }

        httpGet.releaseConnection();

        return toString(responseEntity);

    }

    private HttpGet newHttpGetMethod(String url) {
        HttpGet method = new HttpGet(url);
        method.addHeader(verboiceAuthenticationHeader);
        method.addHeader("Accept", "application/json");

        HttpConnectionParams.setConnectionTimeout(method.getParams(), 30000);
        HttpConnectionParams.setSoTimeout(method.getParams(), 30000);

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
