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
import net.opengis.ows.x20.DomainMetadataType;
import net.opengis.wps.x20.DataDescriptionType;
import net.opengis.wps.x20.FormatDocument;
import net.opengis.wps.x20.InputDescriptionType;
import net.opengis.wps.x20.LiteralDataType;
import net.opengis.wps.x20.OutputDescriptionType;
import net.opengis.wps.x20.ProcessDescriptionType;
import net.opengis.wps.x20.ProcessOfferingDocument;
import net.opengis.wps.x20.ProcessOfferingsDocument;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class WPSProcessesJsonModule extends AbstractWPSJsonModule {

    private static final String PROCESS_OFFERING = "ProcessOffering";
    private static final String _OUTPUT_TRANSMISSION = "_outputTransmission";
    private static final String _JOB_CONTROL_OPTIONS = "_jobControlOptions";
    private static final String _PROCESS_VERSION = "_processVersion";
    private static final String PROCESS = "Process";
    private static final String OUTPUT = "Output";
    private static final String INPUT = "Input";
    private static final String IDENTIFIER = "Identifier";
    private static final String TITLE = "Title";
    private static final String _MAX_OCCURS = "_maxOccurs";
    private static final String _MIN_OCCURS = "_minOccurs";
    private static final String COMPLEX_DATA = "ComplexData";
    private static final String LITERAL_DATA = "LiteralData";
    private static final String LITERAL_DATA_DOMAIN = "LiteralDataDomain";
    private static final String FORMAT = "Format";
    private static final String _MAXIMUM_MEGABYTES = "_maximumMegabytes";
    private static final String _SCHEMA = "_schema";
    private static final String _ENCODING = "_encoding";
    private static final String _MIME_TYPE = "_mimeType";
    private static final String _DEFAULT = "_default";
    private static final String DATA_TYPE = "DataType";
    private static final String ALLOWED_VALUES = "AllowedValues";
    private static final String ANY_VALUE = "AnyValue";
    private static final String _REFERENCE = "_reference";

    /**
     * Constructor.
     */
    public WPSProcessesJsonModule() {
        addSerializer(new ProcessOfferingSerializer());
        addSerializer(new ProcessOfferingsSerializer());
        addSerializer(new ProcessDescriptionSerializer());
        addSerializer(new InputDescriptionSerializer());
        addSerializer(new OutputDescriptionSerializer());
        addSerializer(new DataDescriptionSerializer());
        addSerializer(new FormatSerializer());
        addSerializer(new LiteralDataDomainSerializer());
        addSerializer(new DomainMetadataTypeSerializer());
    }

    private static final class ProcessOfferingsDocumentSerializer extends JsonSerializer<ProcessOfferingsDocument> {

        @Override
        public void serialize(ProcessOfferingsDocument t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {

        }

        @Override
        public Class<ProcessOfferingsDocument> handledType() {
            return ProcessOfferingsDocument.class;
        }

    }

    private static final class ProcessOfferingsSerializer extends JsonSerializer<ProcessOfferingsDocument.ProcessOfferings> {

        @Override
        public void serialize(ProcessOfferingsDocument.ProcessOfferings t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeObjectField(PROCESS_OFFERING, t.getProcessOfferingArray(0));
            jg.writeEndObject();
        }

        @Override
        public Class<ProcessOfferingsDocument.ProcessOfferings> handledType() {
            return ProcessOfferingsDocument.ProcessOfferings.class;
        }
    }

    private static final class ProcessOfferingSerializer extends JsonSerializer<ProcessOfferingDocument.ProcessOffering> {

        @Override
        public void serialize(ProcessOfferingDocument.ProcessOffering t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeObjectField(PROCESS, t.getProcess());
            jg.writeStringField(_PROCESS_VERSION, t.getProcessVersion());
            jg.writeStringField(_JOB_CONTROL_OPTIONS, t.getJobControlOptions().get(0).toString());
            jg.writeStringField(_OUTPUT_TRANSMISSION, t.getOutputTransmission().get(0).toString());
            jg.writeEndObject();
        }

        @Override
        public Class<ProcessOfferingDocument.ProcessOffering> handledType() {
            return ProcessOfferingDocument.ProcessOffering.class;
        }
    }

    private static final class ProcessDescriptionSerializer extends JsonSerializer<ProcessDescriptionType> {

        @Override
        public void serialize(ProcessDescriptionType t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(TITLE, t.getTitleArray(0).getStringValue());
            jg.writeStringField(IDENTIFIER, t.getIdentifier().getStringValue());
            writeArrayOfObjects(INPUT, t.getInputArray(), jg);
            writeArrayOfObjects(OUTPUT, t.getOutputArray(), jg);
            jg.writeEndObject();
        }

        @Override
        public Class<ProcessDescriptionType> handledType() {
            return ProcessDescriptionType.class; //To change body of generated methods, choose Tools | Templates.
        }
    }

    private static final class InputDescriptionSerializer extends JsonSerializer<InputDescriptionType> {

        @Override
        public void serialize(InputDescriptionType t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(TITLE, t.getTitleArray(0).getStringValue());
            jg.writeStringField(IDENTIFIER, t.getIdentifier().getStringValue());

            DataDescriptionType dataDescription = t.getDataDescription();
            if (dataDescription instanceof LiteralDataType) {
                jg.writeObjectField(LITERAL_DATA, t.getDataDescription());
            } else {
                jg.writeObjectField(COMPLEX_DATA, t.getDataDescription());
            }

            jg.writeStringField(_MIN_OCCURS, t.getMinOccurs().toString());
            jg.writeStringField(_MAX_OCCURS, t.getMaxOccurs().toString());
            jg.writeEndObject();
        }

        @Override
        public Class<InputDescriptionType> handledType() {
            return InputDescriptionType.class;
        }
    }

    private static final class OutputDescriptionSerializer extends JsonSerializer<OutputDescriptionType> {

        @Override
        public void serialize(OutputDescriptionType t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(TITLE, t.getTitleArray(0).getStringValue());
            jg.writeStringField(IDENTIFIER, t.getIdentifier().getStringValue());
            jg.writeObjectField(COMPLEX_DATA, t.getDataDescription());
            jg.writeEndObject();
        }

        @Override
        public Class<OutputDescriptionType> handledType() {
            return OutputDescriptionType.class;
        }

    }

    private static final class DataDescriptionSerializer extends JsonSerializer<DataDescriptionType> {

        @Override
        public void serialize(DataDescriptionType t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            writeArrayOfObjects(FORMAT, t.getFormatArray(), jg);
            if (t instanceof LiteralDataType) {
                writeArrayOfObjects(LITERAL_DATA_DOMAIN, ((LiteralDataType) t).getLiteralDataDomainArray(), jg);
            }
            jg.writeEndObject();
        }

        @Override
        public Class<DataDescriptionType> handledType() {
            return DataDescriptionType.class;
        }
    }

    private static final class FormatSerializer extends JsonSerializer<FormatDocument.Format> {

        @Override
        public void serialize(FormatDocument.Format t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(_DEFAULT, "" + t.getDefault());
            jg.writeStringField(_MIME_TYPE, t.getMimeType());
            writeStringFieldIfNotNull(jg, _ENCODING, t.getEncoding());
            writeStringFieldIfNotNull(jg, _SCHEMA, t.getSchema());
            writeStringFieldIfNotNull(jg, _MAXIMUM_MEGABYTES, t.getMaximumMegabytes());
            jg.writeEndObject();
        }

        @Override
        public Class<FormatDocument.Format> handledType() {
            return FormatDocument.Format.class;
        }
    }

    private static final class LiteralDataDomainSerializer extends JsonSerializer<LiteralDataType.LiteralDataDomain> {

        @Override
        public void serialize(LiteralDataType.LiteralDataDomain t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(ANY_VALUE, t.getAnyValue() == null ? null : "");
//            jg.writeStringField(ALLOWED_VALUES, t.getAllowedValues() == null ? null : "");
//            writeStringFieldIfNotNull(jg, ALLOWED_VALUES, t.getAllowedValues());//TODO implement serializer
            jg.writeObjectField(DATA_TYPE, t.getDataType());
            jg.writeEndObject();
        }

        @Override
        public Class<LiteralDataType.LiteralDataDomain> handledType() {
            return LiteralDataType.LiteralDataDomain.class;
        }
    }

    private static final class DomainMetadataTypeSerializer extends JsonSerializer<DomainMetadataType> {

        @Override
        public void serialize(DomainMetadataType t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(_REFERENCE, t.getReference());
            jg.writeEndObject();
        }

        @Override
        public Class<DomainMetadataType> handledType() {
            return DomainMetadataType.class;
        }
    }

    private static final class ProcessOfferingDeserializer extends JsonSerializer<ProcessOfferingDocument> {

        @Override
        public void serialize(ProcessOfferingDocument t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeObjectField(PROCESS_OFFERING, t.getProcessOffering());
            jg.writeEndObject();
        }

        @Override
        public Class<ProcessOfferingDocument> handledType() {
            return ProcessOfferingDocument.class;
        }
    }

    private static final class ProcessDeserializer extends JsonSerializer<ProcessOfferingDocument.ProcessOffering> {

        @Override
        public void serialize(ProcessOfferingDocument.ProcessOffering t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeObjectField(PROCESS, t.getProcess());
            jg.writeEndObject();
        }

        @Override
        public Class<ProcessOfferingDocument.ProcessOffering> handledType() {
            return ProcessOfferingDocument.ProcessOffering.class;
        }
    }
}
