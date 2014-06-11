package org.celllife.ivr.framework.restclient

import org.springframework.beans.factory.annotation.Value;

class RESTClient {

    @Value('${internal.username}')
    def String username;

    @Value('${internal.password}')
    def String password;

    def get(String uri) {

        def client = new groovyx.net.http.RESTClient(uri)
        client.auth.basic(username, password)

        return client.get([:]).data
    }

    def get(String uri, Map<String, Object> query) {

        def client = new groovyx.net.http.RESTClient(uri)
        client.auth.basic(username, password)

        return client.get(query:query).data
    }

    def post(String uri, Map<String, Object> query) {

        def client = new groovyx.net.http.RESTClient(uri)
        client.auth.basic(username, password)

        return client.post(query:query).data

    }

}
