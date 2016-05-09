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

package org.n52.restfulwpsproxy.webapp.rest;

import javax.servlet.http.HttpServletRequest;
import net.opengis.wps.x20.StatusInfoDocument;
import org.n52.restfulwpsproxy.serializer.json.WPSStatusJsonModule;
import org.n52.restfulwpsproxy.wps.GetStatusClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
@RestController
@RequestMapping("/processes/{processId:.+}/jobs/{jobId:.+}")
public class JobController {

    private final GetStatusClient client;

    /**
     * Constructor.
     *
     * @param client wps client for status requests.
     */
    @Autowired
    public JobController(GetStatusClient client) {
        this.client = client;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getStatus(@PathVariable("processId") String processId, @PathVariable("jobId") String jobId, HttpServletRequest request) {
        StatusInfoDocument statusInfo = client.getStatusInfo(processId, jobId);
        return ResponseEntity.ok(new WPSStatusJsonModule.StatusInfoWrapperWithOutput(
                request.getRequestURL().append("/outputs").toString(), statusInfo));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteJob(@PathVariable("processId") String processId, @PathVariable("jobId") String jobId, HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }
    
    @RequestMapping(value = "/outputs", method = RequestMethod.GET)
    public ResponseEntity getOutputs(@PathVariable("processId") String processId, @PathVariable("jobId") String jobId, HttpServletRequest request) {
        return ResponseEntity.ok(client.getResults(processId, jobId));
    }

}
