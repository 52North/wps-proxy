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
            jg.writeStringField(EXPIRATION_DATE, toStringOrNull(t.getExpirationDate()));
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
            jg.writeObjectField(REFERENCE, t.getReference());
            jg.writeObjectField(OUTPUT, t.getOutput());
            jg.writeObjectField(DATA, t.getData());
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
                jg.writeStringField(_ENCODING, t.getEncoding());
                jg.writeStringField(_SCHEMA, t.getSchema());

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

}
