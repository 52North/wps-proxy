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

package org.n52.restfulwpsproxy.rest;

import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import net.opengis.wps.x20.ExecuteDocument;
import net.opengis.wps.x20.ExecuteRequestType;
import net.opengis.wps.x20.ResultDocument;
import net.opengis.wps.x20.StatusInfoDocument;
import org.n52.restfulwpsproxy.wps.CapabilitiesClient;
import org.n52.restfulwpsproxy.wps.ExecuteClient;
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

    @Autowired
    public ProcessController(ExecuteClient executeClient,
            ProcessesClient processesClient,
            CapabilitiesClient capabilitiesClient) {
        this.processesClient = processesClient;
        this.executeClient = executeClient;
        this.capabilitiesClient = capabilitiesClient;
    }

    @RequestMapping(value = "/{processId:.+}", method = RequestMethod.GET)
    public ResponseEntity describeProcess(
            @PathVariable("processId") String processId,
            HttpServletRequest request) throws URISyntaxException {
        return ResponseEntity.ok(processesClient
                .getProcessDescription(processId)
                .getProcessOfferings());
    }

    @RequestMapping(value = "/{processId:.+}", method = RequestMethod.POST)
    public ResponseEntity execute(
            @PathVariable("processId") String processId,
            @RequestParam(value = "sync-execute", required = false, defaultValue = "false") boolean syncExecute,
            @RequestBody ExecuteDocument executeDocument,
            HttpServletRequest request) throws URISyntaxException {

        if (!syncExecute) {
            executeDocument.getExecute().setMode(ExecuteRequestType.Mode.Enum.forString("async"));
            StatusInfoDocument asyncExecute = executeClient.asyncExecute(processId, executeDocument);

            URI created = URI.create(
                    request.getRequestURL()
                    .append("/jobs/")
                    .append(asyncExecute.getStatusInfo().getJobID())
                    .toString());

            return ResponseEntity.created(created).build();
        } else {
            executeDocument.getExecute().setMode(ExecuteRequestType.Mode.Enum.forString("sync"));
            ResultDocument execute = executeClient.syncExecute(processId, executeDocument);

            return ResponseEntity.ok(execute);
        }
    }
}
