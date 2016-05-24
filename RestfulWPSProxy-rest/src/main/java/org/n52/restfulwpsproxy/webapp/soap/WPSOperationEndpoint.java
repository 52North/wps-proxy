package org.n52.restfulwpsproxy.webapp.soap;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
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
            
            XmlOptions xmlOptions = new XmlOptions();
            xmlOptions.setSaveNoXmlDecl();
            
            XmlObject xmlRes = XmlObject.Factory.parse(res, xmlOptions);
            
            DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document wpsResponseDoc = builder.parse(xmlRes.newInputStream());
            
            // Set the response
            message.setAny(wpsResponseDoc.getDocumentElement());
            response.setWpsResponse(message);
        } catch (TransformerFactoryConfigurationError | TransformerException | XmlException | ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
