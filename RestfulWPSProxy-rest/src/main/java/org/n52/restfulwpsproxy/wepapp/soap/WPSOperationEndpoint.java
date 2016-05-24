package org.n52.restfulwpsproxy.wepapp.soap;

import org.n52.restfulwpsproxy.webapp.soap.gen.message.WPSResponse;
import org.n52.restfulwpsproxy.webapp.soap.gen.service.WPSOperationRequest;
import org.n52.restfulwpsproxy.webapp.soap.gen.service.WPSOperationResponse;
import org.n52.restfulwpsproxy.wps.CapabilitiesClient;
import org.n52.restfulwpsproxy.wps.ExecuteClient;
import org.n52.restfulwpsproxy.wps.GetStatusClient;
import org.n52.restfulwpsproxy.wps.ProcessesClient;
import org.n52.restfulwpsproxy.wps.SimplePostClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
@Endpoint
public class WPSOperationEndpoint {

    private static final String TARGET_NAMESPACE = "http://org/n52/restfulwpsproxy/webapp/soap/gen/service";

    private final CapabilitiesClient capabilitiesClient;
    private final ExecuteClient executeClient;
    private final GetStatusClient getStatusClient;
    private final ProcessesClient processesClient;
    private final SimplePostClient postClient;

    // ENDPOINT WSDL can be found under <baseUrl>/endpoints/WPSOperations.wsdl 
    @Autowired
    public WPSOperationEndpoint(CapabilitiesClient capabilitiesClient,
            ExecuteClient executeClient,
            GetStatusClient getStatusClient,
            ProcessesClient processesClient,
            SimplePostClient postClient) {
        this.capabilitiesClient = capabilitiesClient;
        this.getStatusClient = getStatusClient;
        this.executeClient = executeClient;
        this.processesClient = processesClient;
        this.postClient = postClient;
    }

    @PayloadRoot(localPart = "WPSOperationRequest", namespace = TARGET_NAMESPACE)
    public @ResponsePayload WPSOperationResponse performWPSOperation(@RequestPayload WPSOperationRequest request) {
        
        // Initialize the response object.
        WPSOperationResponse response = new WPSOperationResponse();
        WPSResponse message = new WPSResponse();

        // Perform the request
        String res = postClient.performPostRequest(request.getWpsRequest().getRequest());
        
        message.setResponse(res);
        response.setWpsResponse(message);
        
        return response;
    }
//    @PayloadRoot(localPart = "WPSOperationRequest", namespace = TARGET_NAMESPACE)
//    public @ResponsePayload
//    WPSOperationResponse performWPSOperation(
//            @RequestPayload WPSOperationRequest request) {
//
//        // Initialize the response object.
//        WPSOperationResponse response = new WPSOperationResponse();
//        WPSResponse message = new WPSResponse();
//
//        // Get the nested request
//        WPSRequest wpsRequest = request.getWpsRequest();
//        String processID = wpsRequest.getProcessID();
//        String jobID = wpsRequest.getJobID();
//        String executeRequest = wpsRequest.getExecuteRequest();
//
//        // 
//        switch (wpsRequest.getRequestType()) {
//            case "GetCapabilities":
//                message.setResponse(capabilitiesClient.get().xmlText());
//                break;
//            case "GetStatus":
//                message.setResponse(getStatusClient.getStatusInfo(processID, jobID).xmlText());
//                break;
//            case "Execute":
//                message.setResponse(executeClient.asyncExecute(processID, executeRequest).xmlText());
//                break;
//            case "DescribeProcess":
//                message.setResponse(processesClient.getProcessDescription(processID).xmlText());
//                break;
//            case "GetResult":
//                message.setResponse(getStatusClient.getResults(processID, jobID).xmlText());
//                break;
//            default:
//                break;
//        }
//
//        response.setWpsResponse(message);
//        return response;
//    }
}
