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
package org.n52.restfulwpsproxy.serializer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        {
            add(CapabilitiesDocument.class);
            add(ProcessOfferingsDocument.class);
            add(ExecuteDocument.class);
            add(ExecuteDocumentImpl.class);
            add(StatusInfoDocument.class);
            add(ResultDocument.class);
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
            }
        } catch (XmlException ex) {
            Logger.getLogger(WPSBeansMessageConverter.class.getName()).log(Level.SEVERE, null, ex);
            throw new HttpMessageNotReadableException(ex.getMessage(), ex);
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

}
