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
package org.n52.restfulwpsproxy.webapp.soap;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.n52.restfulwpsproxy.webapp.soap.gen.message.WPSResponse;
import org.n52.restfulwpsproxy.webapp.soap.gen.service.WPSOperationRequest;
import org.n52.restfulwpsproxy.webapp.soap.gen.service.WPSOperationResponse;
import org.n52.restfulwpsproxy.wps.SimplePostClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
@Endpoint
public class WPSOperationEndpoint {

    private static final String TARGET_NAMESPACE = "http://org/n52/restfulwpsproxy/webapp/soap/gen/service";

    private final SimplePostClient postClient;

    // ENDPOINT WSDL can be found under <baseUrl>/endpoints/WPSOperations.wsdl 
    @Autowired
    public WPSOperationEndpoint(SimplePostClient postClient) {
        this.postClient = postClient;
    }

    @PayloadRoot(localPart = "WPSOperationRequest", namespace = TARGET_NAMESPACE)
    public @ResponsePayload
    WPSOperationResponse performWPSOperation(@RequestPayload WPSOperationRequest request) {

        // Initialize the response object.
        WPSOperationResponse response = new WPSOperationResponse();
        WPSResponse message = new WPSResponse();

        // Perform the request
        String res;
        try {
            res = postClient.performPostRequest(request.getWpsRequest().getAny());
            
            DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document wpsResponseDoc = builder.parse(new ByteArrayInputStream(res.getBytes()));
            
            // Set the response
            message.setAny(wpsResponseDoc.getDocumentElement());
            response.setWpsResponse(message);
        } catch (TransformerFactoryConfigurationError | TransformerException | ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
