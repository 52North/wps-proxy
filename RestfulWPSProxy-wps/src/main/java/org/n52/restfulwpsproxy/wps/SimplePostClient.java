/*
 * Copyright (C) 2016 by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
