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
import net.opengis.wps.x20.StatusInfoDocument;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class WPSStatusJsonModule extends AbstractWPSJsonModule {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4791629776470211329L;
	private static final String STATUS_INFO = "StatusInfo";
    private static final String PROGRESS = "Progress";
    private static final String OUTPUT = "Output";
    private static final String EXCEPTION = "Exception";
    private static final String SUCCEEDED = "Succeeded";
    private static final Object FAILED = "Failed";
    private static final String STATUS = "Status";
    private static final String JOB_ID = "JobID";

    public static class StatusInfoWrapperWithOutput {

        private final String outputUrl;
        private final StatusInfoDocument statusInfoDocument;

        /**
         * Constructor.
         *
         * @param outputUrl the ID of the process.
         * @param statusInfoDocument the statusinfo xmlbean
         */
        public StatusInfoWrapperWithOutput(String outputUrl, StatusInfoDocument statusInfoDocument) {
            this.outputUrl = outputUrl;
            this.statusInfoDocument = statusInfoDocument;
        }

        public String getOutputUrl() {
            return outputUrl;
        }

        public StatusInfoDocument getStatusInfoDocument() {
            return statusInfoDocument;
        }
    }

    /**
     * Default Constructor.
     */
    public WPSStatusJsonModule() {
        addSerializer(new StatusInfoDocSerializer());
        addSerializer(new StatusInfoSerializer());
        addSerializer(new StatusInfoWrapperSerializer());
    }

    private static final class StatusInfoWrapperSerializer extends JsonSerializer<StatusInfoWrapperWithOutput> {

		@Override
        public void serialize(StatusInfoWrapperWithOutput t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            StatusInfoDocument.StatusInfo statusInfo = t.getStatusInfoDocument().getStatusInfo();

            jg.writeStartObject();
            jg.writeObjectFieldStart(STATUS_INFO);
            jg.writeStringField(JOB_ID, statusInfo.getJobID());
            jg.writeStringField(STATUS, statusInfo.getStatus());
            if (statusInfo.getStatus().equals(SUCCEEDED)) {
                jg.writeStringField(OUTPUT, t.getOutputUrl());
            } else if(statusInfo.getStatus().equals(FAILED)) {
                jg.writeStringField(EXCEPTION, t.getOutputUrl());//TODO build getresultURL
            }else{
                jg.writeNumberField(PROGRESS, statusInfo.getPercentCompleted());            	
            }
            jg.writeEndObject();
            jg.writeEndObject();
        }

        @Override
        public Class<StatusInfoWrapperWithOutput> handledType() {
            return StatusInfoWrapperWithOutput.class;
        }
    }

    private static final class StatusInfoDocSerializer extends JsonSerializer<StatusInfoDocument> {

        @Override
        public void serialize(StatusInfoDocument t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeObjectField(STATUS_INFO, t.getStatusInfo());
            jg.writeEndObject();
        }

        @Override
        public Class<StatusInfoDocument> handledType() {
            return StatusInfoDocument.class;
        }
    }

    private static final class StatusInfoSerializer extends JsonSerializer<StatusInfoDocument.StatusInfo> {

        @Override
        public void serialize(StatusInfoDocument.StatusInfo t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(JOB_ID, t.getJobID());
            jg.writeStringField(STATUS, t.getStatus());
            jg.writeNumberField(PROGRESS, t.getPercentCompleted());
            jg.writeEndObject();
        }

        @Override
        public Class<StatusInfoDocument.StatusInfo> handledType() {
            return StatusInfoDocument.StatusInfo.class;
        }
    }
}
