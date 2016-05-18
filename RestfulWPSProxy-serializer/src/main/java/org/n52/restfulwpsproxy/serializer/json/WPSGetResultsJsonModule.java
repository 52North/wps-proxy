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
package org.n52.restfulwpsproxy.serializer.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import net.opengis.wps.x20.DataDocument;
import net.opengis.wps.x20.DataOutputType;
import net.opengis.wps.x20.ReferenceType;
import net.opengis.wps.x20.ResultDocument;
import static org.n52.restfulwpsproxy.serializer.json.AbstractWPSJsonModule.toStringOrNull;
import org.n52.restfulwpsproxy.util.XMLBeansHelper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class WPSGetResultsJsonModule extends AbstractWPSJsonModule {

    private static final String RESULT = "Result";
    private static final String OUTPUT = "Output";
    private static final String EXPIRATION_DATE = "ExpirationDate";
    private static final String JOB_ID = "JobID";
    private static final String DATA = "Data";
    private static final String REFERENCE = "Reference";
    private static final String _HREF = "_href";
    private static final String ID = "ID";
    private static final String _TEXT = "_text";
    private static final String _SCHEMA = "_schema";
    private static final String _ENCODING = "_encoding";
    private static final String _MIME_TYPE = "_mimeType";

    public WPSGetResultsJsonModule() {
        addSerializer(new ResultDocumentSerializer());
        addSerializer(new ResultSerializer());
        addSerializer(new DataOutputTypeSerialzer());
        addSerializer(new DataSerializer());
        addSerializer(new ReferenceSerializer());
    }

    private static final class ResultDocumentSerializer extends JsonSerializer<ResultDocument> {

        @Override
        public void serialize(ResultDocument t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeObjectField(RESULT, t.getResult());
            jg.writeEndObject();
        }

        @Override
        public Class<ResultDocument> handledType() {
            return ResultDocument.class;
        }
    }

    private static final class ResultSerializer extends JsonSerializer<ResultDocument.Result> {

        @Override
        public void serialize(ResultDocument.Result t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(JOB_ID, t.getJobID());
            writeStringFieldIfNotNull(jg, EXPIRATION_DATE, t.getExpirationDate());
            writeArrayOfObjects(OUTPUT, t.getOutputArray(), jg);
            jg.writeEndObject();
        }

        @Override
        public Class<ResultDocument.Result> handledType() {
            return ResultDocument.Result.class;
        }
    }

    private static final class DataOutputTypeSerialzer extends JsonSerializer<DataOutputType> {

        @Override
        public void serialize(DataOutputType t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeObjectField(ID, t.getId());
            writeObjectFieldIfNotNull(jg, REFERENCE, t.getReference());
            writeObjectFieldIfNotNull(jg, OUTPUT, t.getOutput());
            writeObjectFieldIfNotNull(jg, DATA, t.getData());
            jg.writeEndObject();
        }

        @Override
        public Class<DataOutputType> handledType() {
            return DataOutputType.class;
        }
    }

    private static final class DataSerializer extends JsonSerializer<DataDocument.Data> {

        @Override
        public void serialize(DataDocument.Data t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            try {
                jg.writeStartObject();
                jg.writeStringField(_MIME_TYPE, t.getMimeType());
                writeStringFieldIfNotNull(jg, _ENCODING, t.getEncoding());
                writeStringFieldIfNotNull(jg, _SCHEMA, t.getSchema());

                NodeList candidateNodes = t.getDomNode().getChildNodes();
                Node complexDataNode = candidateNodes.getLength() > 1 ? candidateNodes.item(1) : candidateNodes.item(0);
                jg.writeStringField(_TEXT, XMLBeansHelper.nodeToString(complexDataNode));

                jg.writeEndObject();
            } catch (TransformerFactoryConfigurationError | TransformerException ex) {
                Logger.getLogger(WPSGetResultsJsonModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public Class<DataDocument.Data> handledType() {
            return DataDocument.Data.class;
        }
    }

    private static final class ReferenceSerializer extends JsonSerializer<ReferenceType> {

        @Override
        public void serialize(ReferenceType r,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {

            jg.writeStartObject();
            jg.writeStringField(_MIME_TYPE, r.getMimeType());
            writeStringFieldIfNotNull(jg, _ENCODING, r.getEncoding());
            writeStringFieldIfNotNull(jg, _SCHEMA, r.getSchema());
            jg.writeStringField(_HREF, r.getHref());

            jg.writeEndObject();
            
        }

        @Override
        public Class<ReferenceType> handledType() {
            return ReferenceType.class;
        }

    }

}
