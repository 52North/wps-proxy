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

import javax.servlet.http.HttpServletRequest;

import org.n52.restfulwpsproxy.serializer.json.WPSStatusJsonModule;
import org.n52.restfulwpsproxy.serializer.json.WPSStatusJsonModule.StatusInfoWrapperWithOutput;
import org.n52.restfulwpsproxy.wps.GetJobsClient;
import org.n52.restfulwpsproxy.wps.GetStatusClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.wps.x20.ResultDocument;
import net.opengis.wps.x20.StatusInfoDocument;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
@RestController
@RequestMapping("/processes/{processId:.+}/jobs")
public class JobController {

    private final GetStatusClient client;
    private final GetJobsClient getJobsClient;

    /**
     * Constructor.
     *
     * @param client wps client for status requests.
     */
    @Autowired
    public JobController(GetStatusClient client, GetJobsClient getJobsClient) {
        this.client = client;
        this.getJobsClient = getJobsClient;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String[]> getJobs(@PathVariable("processId") String processId, HttpServletRequest request) {
        String[] jobIds = getJobsClient.getJobIds(processId);
        return ResponseEntity.ok(jobIds);
    }
    
    @RequestMapping(value = "/{jobId:.+}", method = RequestMethod.GET)
    public ResponseEntity<StatusInfoWrapperWithOutput> getStatus(@PathVariable("processId") String processId, @PathVariable("jobId") String jobId, HttpServletRequest request) {
        StatusInfoDocument statusInfo = client.getStatusInfo(processId, jobId);
        return ResponseEntity.ok(new WPSStatusJsonModule.StatusInfoWrapperWithOutput(
                request.getRequestURL().toString(), statusInfo));
    }

    @RequestMapping(value = "/{jobId:.+}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteJob(@PathVariable("processId") String processId, @PathVariable("jobId") String jobId, HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }
    
    @RequestMapping(value = "/{jobId:.+}/outputs", method = RequestMethod.GET)
    public ResponseEntity<ResultDocument> getOutputs(@PathVariable("processId") String processId, @PathVariable("jobId") String jobId, HttpServletRequest request) {
        return ResponseEntity.ok(client.getResults(processId, jobId));
    }
    
    @RequestMapping(value = "/{jobId:.+}/exceptions", method = RequestMethod.GET)
    public ResponseEntity<ExceptionReportDocument> getExceptions(@PathVariable("processId") String processId, @PathVariable("jobId") String jobId, HttpServletRequest request) {
    	return ResponseEntity.ok(client.getExceptions(processId, jobId));
    }

}
