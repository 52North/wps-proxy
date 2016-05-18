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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import net.opengis.wps.x20.DataDocument;
import net.opengis.wps.x20.DataInputType;
import net.opengis.wps.x20.DataTransmissionModeType;
import net.opengis.wps.x20.ExecuteDocument;
import net.opengis.wps.x20.ExecuteRequestType;
import net.opengis.wps.x20.OutputDefinitionType;
import net.opengis.wps.x20.ReferenceType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.n52.restfulwpsproxy.serializer.WPSBeansObjectMapper;
import org.n52.restfulwpsproxy.util.XMLBeansHelper;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class WPSExecuteJsonModule extends AbstractWPSJsonModule {

    private static WPSBeansObjectMapper mapper;

    public WPSExecuteJsonModule(WPSBeansObjectMapper mapper) {
        addDeserializer(ExecuteDocument.class, new ExecuteDeserializer());
        addDeserializer(ExecuteRequestType.class, new ExecuteRequestTypeDeserializer());
        addDeserializer(OutputDefinitionType.class, new OutputDefinitionTypeDeserializer());
        addDeserializer(DataInputType.class, new DataInputTypeDeserializer());
        addDeserializer(ReferenceType.class, new ReferenceTypeDeserializer());
        addDeserializer(DataDocument.Data.class, new DataTypeSerializer());
        this.mapper = mapper;
    }

    private static abstract class WPSBeanDeserializer<T extends XmlObject> extends JsonDeserializer<T> {

        @Override
        public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            T newInstance = newInstance();
            while (jp.nextToken() != null) {
                switch (jp.getCurrentToken()) {
                    case START_OBJECT:
                        if (jp.getCurrentName() == null) {
                            continue;
                        }
                        readObject(newInstance, jp.getCurrentName(), jp);
                        break;
                    case START_ARRAY:
                        readArray(newInstance, jp.getCurrentName(), jp);
                        break;
                    case VALUE_STRING:
                        if (jp.getCurrentName() == null) {
                            continue;
                        }
                        readString(newInstance, jp.getCurrentName(), jp);
                        break;
                }
            }
            return newInstance;
        }

        protected <E> E[] readObjectAsArray(Class<E> clazz, JsonParser jp) throws IOException {
            E value = jp.readValueAs(clazz);
            final E[] res = (E[]) Array.newInstance(clazz, 1);
            res[0] = value;
            return res;
        }

        protected <E> E[] readObjectsAsArray(Class<E> clazz, JsonParser jp) throws IOException {
            List<E> list = new ArrayList<>();
            jp.readValuesAs(clazz).forEachRemaining(list::add);
            final E[] res = (E[]) Array.newInstance(clazz, list.size());
            for (int i = 0; i < list.size(); i++) {
                res[i] = list.get(i);
            }
            return res;
        }

        protected abstract T newInstance();

        protected abstract void readObject(T instance, String currentName, JsonParser jp) throws IOException;

        protected void readArray(T instance, String currentName, JsonParser jp) throws IOException {
        }

        protected abstract void readString(T instance, String currentName, JsonParser jp) throws IOException;
    }

    private static final class ExecuteDeserializer extends JsonDeserializer<ExecuteDocument> {

        @Override
        public ExecuteDocument deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
            ExecuteDocument executeDocument = ExecuteDocument.Factory.newInstance(XMLBeansHelper.getWPSXmlOptions());
            while (jp.nextToken() != null) {
                switch (jp.getCurrentToken()) {
                    case START_OBJECT:
                        if (jp.getCurrentName().equals("Execute")) {
                            executeDocument.setExecute(jp.readValueAs(ExecuteRequestType.class));
                        }
                        break;
                }
            }

            return executeDocument;
        }

        @Override
        public Class<ExecuteDocument> handledType() {
            return ExecuteDocument.class;
        }
    }

    private static abstract class AbstractWPSDeserializer<T extends XmlObject> extends JsonDeserializer<T> {

        @Override
        public T deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
            T newInstance = getInstance();
            jp.setCodec(mapper);
            JsonNode node = jp.getCodec().readTree(jp);
            deserialize(jp, node, newInstance);
            return newInstance;
        }

        public abstract T getInstance();

        public abstract void deserialize(JsonParser jp, JsonNode node, T instance) throws IOException, JsonProcessingException;

        protected <E> E[] readArrayValues(String value, JsonNode node, Class<?> clazz, TypeReference<List<E>> typeRef) throws IOException {
            JsonParser traverse = node.get(value).traverse();
            List<E> readValues = mapper.readValue(traverse, typeRef);
            return toArray(readValues, clazz);
        }

        protected <E> E[] toArray(List<E> list, Class<?> clazz) {
            E[] res = (E[]) Array.newInstance(clazz, list.size());

            int i = 0;
            for (E entry : list) {
                res[i] = entry;
                i++;
            }
            return res;
        }

        protected XmlString getAsXmlString(String key, JsonNode node) {
            XmlString xmlString = XmlString.Factory.newInstance();
            xmlString.set(node.get(key).asText());
            return xmlString;
        }
    }

    private static final class ExecuteDocumentDeserializer extends AbstractWPSDeserializer<ExecuteDocument> {

        @Override
        public ExecuteDocument getInstance() {
            return ExecuteDocument.Factory.newInstance();
        }

        @Override
        public void deserialize(JsonParser jp, JsonNode node, ExecuteDocument instance) throws IOException, JsonProcessingException {
            if (node.has("Execute")) {
                instance.setExecute(mapper.readValue(node.get("Reference").traverse(), ExecuteRequestType.class));
            }
        }

    }

    private static final class ExecuteRequestTypeDeserializer extends AbstractWPSDeserializer<ExecuteRequestType> {

        @Override
        public ExecuteRequestType getInstance() {
            ExecuteRequestType requestType = ExecuteRequestType.Factory.newInstance(XMLBeansHelper.getWPSXmlOptions());
            requestType.setService("WPS");
            requestType.setVersion("2.0.0");
            requestType.setResponse(ExecuteRequestType.Response.DOCUMENT);
            return  requestType;
        }

        @Override
        public void deserialize(JsonParser jp, JsonNode node, ExecuteRequestType instance) throws IOException, JsonProcessingException {
            if (node.has("Identifier")) {
                instance.addNewIdentifier().setStringValue(node.get("Identifier").asText());
            }
            if (node.has("Input")) {
                instance.setInputArray(readArrayValues("Input", node, DataInputType.class, new TypeReference<List<DataInputType>>() {
                }));
            }
            if (node.has("output")) {
                instance.setOutputArray(readArrayValues("output", node, OutputDefinitionType.class, new TypeReference<List<OutputDefinitionType>>() {
                }));
            }
            if (node.has("_service")) {
                instance.setService(node.get("_service").asText());
            }
            if (node.has("_version")) {
                instance.setVersion(node.get("_version").asText());
            }
            if (node.has("_response")) {
                instance.setResponse(ExecuteRequestType.Response.Enum.forString(node.get("_response").asText()));
            }
        }
    }

    private static final class DataInputTypeDeserializer extends AbstractWPSDeserializer<DataInputType> {

        @Override
        public DataInputType getInstance() {
            return DataInputType.Factory.newInstance(XMLBeansHelper.getWPSXmlOptions());
        }

        @Override
        public void deserialize(JsonParser jp, JsonNode node, DataInputType instance) throws IOException, JsonProcessingException {
            if (node.has("_id")) {
                instance.setId(node.get("_id").asText());
            }
            if (node.has("Reference")) {
                instance.setReference(mapper
                        .readValue(node.get("Reference").traverse(), ReferenceType.class));
            }
            if (node.has("Data")) {
                instance.setData(mapper
                        .readValue(node.get("Data").traverse(), DataDocument.Data.class));
            }
            if (node.has("Input")) {
                instance.setInputArray(readArrayValues("Input", node, DataInputType.class, new TypeReference<List<DataInputType>>() {
                }));
            }
        }
    }

    private static final class ReferenceTypeDeserializer extends AbstractWPSDeserializer<ReferenceType> {

        @Override
        public ReferenceType getInstance() {
            return ReferenceType.Factory.newInstance(XMLBeansHelper.getWPSXmlOptions());
        }

        @Override
        public void deserialize(JsonParser jp, JsonNode node, ReferenceType instance) throws IOException, JsonProcessingException {
            if (node.has("_href")) {
                instance.setHref(node.get("_href").asText());
            }
            if (node.has("_mimeType")) {
                instance.setMimeType(node.get("_mimeType").asText());
            }
            if (node.has("_schema")) {
                instance.setSchema(node.get("_schema").asText());
            }
            if (node.has("_encoding")) {
                instance.setSchema(node.get("_encoding").asText());
            }
        }
    }

    private static final class DataTypeSerializer extends AbstractWPSDeserializer<DataDocument.Data> {

        @Override
        public DataDocument.Data getInstance() {
            return DataInputType.Factory.newInstance(XMLBeansHelper.getWPSXmlOptions()).addNewData();
        }

        @Override
        public void deserialize(JsonParser jp, JsonNode node, DataDocument.Data instance) throws IOException, JsonProcessingException {
            if (node.has("_text")) {
                instance.set(getAsXmlString("_text", node));
            }
            if (node.has("_mimeType")) {
                instance.setMimeType(node.get("_mimeType").asText());
            }
            if (node.has("_schema")) {
                instance.setSchema(node.get("_schema").asText());
            }
            if (node.has("_encoding")) {
                instance.setEncoding(node.get("_encoding").asText());
            }
        }
    }

    private static final class OutputDefinitionTypeDeserializer extends AbstractWPSDeserializer<OutputDefinitionType> {

        @Override
        public OutputDefinitionType getInstance() {
            return OutputDefinitionType.Factory.newInstance(XMLBeansHelper.getWPSXmlOptions());
        }

        @Override
        public void deserialize(JsonParser jp, JsonNode node, OutputDefinitionType instance) throws IOException, JsonProcessingException {
            if (node.has("_id")) {
                instance.setId(node.get("_id").asText());
            }
            if (node.has("_mimeType")) {
                instance.setMimeType(node.get("_mimeType").asText());
            }
            if (node.has("_schema")) {
                instance.setSchema(node.get("_schema").asText());
            }
            if (node.has("_encoding")) {
                instance.setEncoding(node.get("_encoding").asText());
            }
            if (node.has("_transmission")) {
                instance.setTransmission(DataTransmissionModeType.Enum.forString(node.get("_transmission").asText()));
            }
        }
    }
}
