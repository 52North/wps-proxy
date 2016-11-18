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

import net.opengis.wps.x20.CapabilitiesDocument;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class CapabilitiesClient extends AbstractWPSClient {

    private static final String REQUEST_GET_CAPABILITIES = "GetCapabilities";

    /**
     * Constructor.
     *
     * @param baseUrl
     * @param restTemplate
     */
    public CapabilitiesClient(String baseUrl, RestTemplate restTemplate) {
        super(baseUrl, restTemplate);
    }

    public CapabilitiesDocument get() {
        HttpEntity requestEntity = new HttpEntity(null, headers);

        ResponseEntity<CapabilitiesDocument> capabilities = restTemplate.exchange(
                new RequestUrlBuilder(REQUEST_GET_CAPABILITIES).build(),
                HttpMethod.GET,
                requestEntity,
                CapabilitiesDocument.class);

        return capabilities.getBody();
    }

}
