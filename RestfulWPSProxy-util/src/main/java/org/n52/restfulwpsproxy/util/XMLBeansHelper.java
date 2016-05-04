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
package org.n52.restfulwpsproxy.util;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Node;

/**
 *
 * @author adewa
 */
public class XMLBeansHelper {

    public static final String NS_WPS_2_0 = "http://www.opengis.net/wps/2.0";
    public static final String NS_WPS_PREFIX = "wps";
    public static final String NS_OWS_2_0 = "http://www.opengis.net/ows/2.0";
    public static final String NS_OWS_PREFIX = "ows";
    public static final String NS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String NS_XSI_PREFIX = "xsi";

    private static Map<String, String> PREFIXES = new HashMap<String, String>() {
        {
            put(NS_WPS_2_0, NS_WPS_PREFIX);
            put(NS_OWS_2_0, NS_OWS_PREFIX);
            put(NS_XSI, NS_XSI_PREFIX);
        }
    };

    public static XmlOptions getWPSXmlOptions() {
        return new XmlOptions()
                .setSaveNamespacesFirst()
                .setSaveSuggestedPrefixes(PREFIXES)
                .setSaveAggressiveNamespaces()
                .setSavePrettyPrint();
    }

    public static String nodeToString(Node node) throws TransformerFactoryConfigurationError, TransformerException {
        StringWriter stringWriter = new StringWriter();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(node), new StreamResult(stringWriter));

        return stringWriter.toString();
    }
}
