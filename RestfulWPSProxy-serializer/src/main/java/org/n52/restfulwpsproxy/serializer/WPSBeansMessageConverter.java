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
package org.n52.restfulwpsproxy.serializer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.opengis.ows.x20.ExceptionReportDocument;
import net.opengis.wps.x20.CapabilitiesDocument;
import net.opengis.wps.x20.ExecuteDocument;
import net.opengis.wps.x20.ProcessOfferingsDocument;
import net.opengis.wps.x20.ResultDocument;
import net.opengis.wps.x20.StatusInfoDocument;
import net.opengis.wps.x20.impl.ExecuteDocumentImpl;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.n52.restfulwpsproxy.util.XMLBeansHelper;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class WPSBeansMessageConverter extends AbstractHttpMessageConverter<XmlObject> {

    private static final List<Class<? extends XmlObject>> supportedTypes = new ArrayList<Class<? extends XmlObject>>() {
        /**
		 * 
		 */
		private static final long serialVersionUID = -3329873321870335320L;

		{
            add(CapabilitiesDocument.class);
            add(ProcessOfferingsDocument.class);
            add(ExecuteDocument.class);
            add(ExecuteDocumentImpl.class);
            add(StatusInfoDocument.class);
            add(ResultDocument.class);
            add(ExceptionReportDocument.class);
        }
    };

    public WPSBeansMessageConverter() {
        super(new MediaType("text", "xml", Charset.defaultCharset()),
                new MediaType("application", "xml", Charset.defaultCharset()));
    }

    @Override
    protected boolean supports(Class<?> type) {
        return supportedTypes.contains(type);
    }

    @Override
    protected void writeInternal(XmlObject t, HttpOutputMessage hom) throws IOException, HttpMessageNotWritableException {
        if (t instanceof ExecuteDocument) {
            t.save(hom.getBody(), XMLBeansHelper.getWPSXmlOptions());
        }
    }

    @Override
    protected XmlObject readInternal(Class<? extends XmlObject> type, HttpInputMessage him) throws IOException, HttpMessageNotReadableException {
        try {
            if (type == CapabilitiesDocument.class) {
                return CapabilitiesDocument.Factory.parse(him.getBody());
            } else if (type == ProcessOfferingsDocument.class) {
                return ProcessOfferingsDocument.Factory.parse(him.getBody());
            } else if (type == StatusInfoDocument.class) {
                return StatusInfoDocument.Factory.parse(him.getBody());
            } else if (type == ResultDocument.class) {
                return ResultDocument.Factory.parse(him.getBody());
            } else if (type == ExceptionReportDocument.class) {
                return ExceptionReportDocument.Factory.parse(him.getBody());
            }
        } catch (XmlException ex) {
            Logger.getLogger(WPSBeansMessageConverter.class.getName()).log(Level.SEVERE, null, ex);
            throw new HttpMessageNotReadableException(ex.getMessage(), ex);
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

}
