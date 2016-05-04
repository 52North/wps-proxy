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
import net.opengis.wps.x20.StatusInfoDocument;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class WPSStatusJsonModule extends AbstractWPSJsonModule {

    private static final String STATUS_INFO = "StatusInfo";
    private static final String PROGRESS = "Progress";
    private static final String OUTPUT = "Output";
    private static final String SUCCEEDED = "Succeeded";
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
            } else {
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
