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
package org.n52.restfulwpsproxy.wps;

import java.net.URISyntaxException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import net.opengis.wps.x20.ExecuteDocument;
import net.opengis.wps.x20.ResultDocument;
import net.opengis.wps.x20.StatusInfoDocument;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class ExecuteClient extends AbstractWPSClient {

    /**
     * Constructor.
     *
     * @param baseUrl
     * @param restTemplate
     */
    public ExecuteClient(String baseUrl, RestTemplate restTemplate) {
        super(baseUrl, restTemplate);
    }

    public StatusInfoDocument asyncExecute(String processId, ExecuteDocument executeDocument) throws URISyntaxException {
        HttpEntity<ExecuteDocument> requestEntity = new HttpEntity<ExecuteDocument>(executeDocument, headers);
        return restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, StatusInfoDocument.class).getBody();
    }

    public StatusInfoDocument asyncExecute(String processId, String executeDocument) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(executeDocument, headers);
        return restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, StatusInfoDocument.class).getBody();
    }

    public ResultDocument syncExecute(String processId, ExecuteDocument executeDocument) throws URISyntaxException {
        HttpEntity<ExecuteDocument> requestEntity = new HttpEntity<ExecuteDocument>(executeDocument, headers);
        return restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, ResultDocument.class).getBody();
    }

}
