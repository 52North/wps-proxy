package org.n52.restfulwpsproxy.wepapp.soap;

import org.n52.restfulwpsproxy.webapp.soap.gen.message.WPSResponse;
import org.n52.restfulwpsproxy.webapp.soap.gen.service.WPSOperationRequest;
import org.n52.restfulwpsproxy.webapp.soap.gen.service.WPSOperationResponse;
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
        String res = postClient.performPostRequest(request.getWpsRequest().getRequest());

        // Set the response
        message.setResponse(res);
        response.setWpsResponse(message);

        return response;
    }
}
