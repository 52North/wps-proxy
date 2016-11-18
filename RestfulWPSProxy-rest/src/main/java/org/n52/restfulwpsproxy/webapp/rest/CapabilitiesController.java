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
package org.n52.restfulwpsproxy.webapp.rest;

import org.n52.restfulwpsproxy.wps.CapabilitiesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO Java
 *
 * @author dewall
 */
@RestController
public class CapabilitiesController {

    private final CapabilitiesClient client;

    @Autowired
    public CapabilitiesController(CapabilitiesClient client) {
        this.client = client;
    }

    /**
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getCapabilities() {
        return ResponseEntity.ok(client.get());
    }

    @RequestMapping(value = "/processes", method = RequestMethod.GET)
    public ResponseEntity processSummaries() {
        return ResponseEntity.ok(
                client.get()
                .getCapabilities()
                .getContents());
    }
}
