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

import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import net.opengis.wps.x20.ExecuteDocument;
import net.opengis.wps.x20.ExecuteRequestType;
import net.opengis.wps.x20.ProcessOfferingsDocument.ProcessOfferings;
import net.opengis.wps.x20.ResultDocument;
import net.opengis.wps.x20.StatusInfoDocument;
import org.n52.restfulwpsproxy.wps.CapabilitiesClient;
import org.n52.restfulwpsproxy.wps.ExecuteClient;
import org.n52.restfulwpsproxy.wps.GetJobsClient;
import org.n52.restfulwpsproxy.wps.ProcessesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
@RestController
@RequestMapping("/processes")
public class ProcessController {

    private final ExecuteClient executeClient;
    private final ProcessesClient processesClient;
    private final CapabilitiesClient capabilitiesClient;
    private final GetJobsClient getJobsClient;

    @Autowired
    public ProcessController(ExecuteClient executeClient,
            ProcessesClient processesClient,
            CapabilitiesClient capabilitiesClient, GetJobsClient getJobsClient) {
        this.processesClient = processesClient;
        this.executeClient = executeClient;
        this.capabilitiesClient = capabilitiesClient;
        this.getJobsClient = getJobsClient;
    }

    @RequestMapping(value = "/{processId:.+}", method = RequestMethod.GET)
    public ResponseEntity<ProcessOfferings> describeProcess(
            @PathVariable("processId") String processId,
            HttpServletRequest request) throws URISyntaxException {
        return ResponseEntity.ok(processesClient
                .getProcessDescription(processId)
                .getProcessOfferings());
    }

    @RequestMapping(value = "/{processId:.+}/jobs", method = RequestMethod.POST)
    public ResponseEntity<?> execute(
            @PathVariable("processId") String processId,
            @RequestParam(value = "sync-execute", required = false, defaultValue = "false") boolean syncExecute,
            @RequestBody ExecuteDocument executeDocument,
            HttpServletRequest request) throws URISyntaxException {

        if (!syncExecute) {
            executeDocument.getExecute().setMode(ExecuteRequestType.Mode.Enum.forString("async"));
            StatusInfoDocument asyncExecute = executeClient.asyncExecute(processId, executeDocument);

            String jobId = asyncExecute.getStatusInfo().getJobID();
            
            URI created = URI.create(
                    request.getRequestURL()
                    .append("/")
                    .append(jobId)
                    .toString());

            getJobsClient.addJobId(processId, jobId);
            
            return ResponseEntity.created(created).build();
        } else {
            executeDocument.getExecute().setMode(ExecuteRequestType.Mode.Enum.forString("sync"));
            ResultDocument execute = executeClient.syncExecute(processId, executeDocument);

            return ResponseEntity.ok(execute);
        }
    }
}
