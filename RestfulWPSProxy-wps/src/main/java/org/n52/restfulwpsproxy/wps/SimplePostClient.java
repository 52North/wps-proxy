/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.n52.restfulwpsproxy.wps;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.n52.restfulwpsproxy.util.XMLBeansHelper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Node;

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

    public String performPostRequest(Object xmlRequest) throws TransformerFactoryConfigurationError,TransformerException {
        HttpEntity requestEntity = new HttpEntity(XMLBeansHelper.nodeToString((Node) xmlRequest), headers);
        ResponseEntity<String> exchange = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, String.class);
        return exchange.getBody();
    }
}
