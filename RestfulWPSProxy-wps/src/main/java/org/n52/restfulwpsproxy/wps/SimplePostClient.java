/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.n52.restfulwpsproxy.wps;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class SimplePostClient extends AbstractWPSClient {

    /**
     * Constructor.
     *
     * @param baseUrl
     * @param restTemplate
     */
    public SimplePostClient(String baseUrl, RestTemplate restTemplate) {
        super(baseUrl, restTemplate);
    }

    public String performPostRequest(String xmlRequest) {
        HttpEntity requestEntity = new HttpEntity(xmlRequest, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, String.class);
        return exchange.getBody();
    }
}
