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

import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.wps.x20.ResultDocument;
import net.opengis.wps.x20.StatusInfoDocument;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class GetStatusClient extends AbstractWPSClient {
    private static final String GET_STATUS = "GetStatus";
    private static final String GET_RESULT = "GetResult";


    /**
     * Constructor.
     *
     * @param baseUrl baseUrl des WPS services.
     * @param restTemplate
     */
    public GetStatusClient(String baseUrl, RestTemplate restTemplate) {
        super(baseUrl, restTemplate);
    }

    public StatusInfoDocument getStatusInfo(String processId, String jobId) {
        HttpEntity<?> requestEntity = new HttpEntity<Object>(null, headers);

        ResponseEntity<StatusInfoDocument> statusInfo = restTemplate
                .exchange(new RequestUrlBuilder(GET_STATUS).jobID(jobId).build(),
                        HttpMethod.GET,
                        requestEntity,
                        StatusInfoDocument.class
                );

        return statusInfo.getBody();
    }

    public ResultDocument getResults(String processId, String jobId) {
        HttpEntity<?> requestEntity = new HttpEntity<Object>(null, headers);

        ResponseEntity<ResultDocument> resultDocument = restTemplate
                .exchange(
                        new RequestUrlBuilder(GET_RESULT).jobID(jobId).build(),
                        HttpMethod.GET,
                        requestEntity,
                        ResultDocument.class
                );

        return resultDocument.getBody();
    }

	public ExceptionReportDocument getExceptions(String processId, String jobId) {
        HttpEntity<?> requestEntity = new HttpEntity<Object>(null, headers);

        ResponseEntity<ExceptionReportDocument> resultDocument = restTemplate
                .exchange(
                        new RequestUrlBuilder(GET_RESULT).jobID(jobId).build(),
                        HttpMethod.GET,
                        requestEntity,
                        ExceptionReportDocument.class
                );

        return resultDocument.getBody();
	}

}
