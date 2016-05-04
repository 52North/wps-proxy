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

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public abstract class AbstractWPSClient {

    private static final String GET_PARAM_VERSION = "version";
    private static final String GET_PARAM_VERSION_DEFAULT = "2.0.0";
    private static final String GET_PARAM_SERVICE = "Service";
    private static final String GET_PARAM_SERVICE_DEFAULT = "WPS";
    private static final String GET_PARAM_REQUEST = "Request";
    private static final String GET_PARAM_IDENTIFIER = "Identifier";
    private static final String GET_PARAM_JOBID = "jobID";

    protected final String baseUrl;
    protected final RestTemplate restTemplate;
    protected final HttpHeaders headers;

    protected class RequestUrlBuilder {

        private final Map<String, String> requestParams = new HashMap<>();

        /**
         * Constructor.
         *
         * @param requestType
         */
        public RequestUrlBuilder(String requestType) {
            this.requestParams.put(GET_PARAM_REQUEST, requestType);
            this.requestParams.put(GET_PARAM_SERVICE, GET_PARAM_SERVICE_DEFAULT);
            this.requestParams.put(GET_PARAM_VERSION, GET_PARAM_VERSION_DEFAULT);
        }

        public RequestUrlBuilder version(String version) {
            this.requestParams.put(GET_PARAM_VERSION, version);
            return this;
        }

        public RequestUrlBuilder service(String service) {
            this.requestParams.put(GET_PARAM_SERVICE, service);
            return this;
        }

        public RequestUrlBuilder identifier(String identifier) {
            this.requestParams.put(GET_PARAM_IDENTIFIER, identifier);
            return this;
        }

        public RequestUrlBuilder jobID(String jobId) {
            this.requestParams.put(GET_PARAM_JOBID, jobId);
            return this;
        }

        public String build() {
            UriComponentsBuilder res = UriComponentsBuilder.fromUriString(baseUrl);
            requestParams.entrySet().stream().forEach((param) -> {
                res.queryParam(param.getKey(), param.getValue());
            });
            return res.build().toUriString();
        }

    }

    /**
     * Constructor.
     *
     * @param baseUrl
     * @param restTemplate
     */
    public AbstractWPSClient(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;

        this.headers = new HttpHeaders();
        this.headers.add("Content-Type", "application/xml");
        this.headers.add("Accept", "application/xml");
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

}
