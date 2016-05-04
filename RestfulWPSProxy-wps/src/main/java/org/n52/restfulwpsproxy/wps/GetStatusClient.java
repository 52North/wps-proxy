/*
 * Copyright (C) 2016
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.restfulwpsproxy.wps;

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
        HttpEntity requestEntity = new HttpEntity(null, headers);

        ResponseEntity<StatusInfoDocument> statusInfo = restTemplate
                .exchange(new RequestUrlBuilder(GET_STATUS).jobID(jobId).build(),
                        HttpMethod.GET,
                        requestEntity,
                        StatusInfoDocument.class
                );

        return statusInfo.getBody();
    }

    public ResultDocument getResults(String processId, String jobId) {
        HttpEntity requestEntity = new HttpEntity(null, headers);

        ResponseEntity<ResultDocument> resultDocument = restTemplate
                .exchange(
                        new RequestUrlBuilder(GET_RESULT).jobID(jobId).build(),
                        HttpMethod.GET,
                        requestEntity,
                        ResultDocument.class
                );

        return resultDocument.getBody();
    }

}
