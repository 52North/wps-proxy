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
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import net.opengis.ows.x20.AddressType;
import net.opengis.ows.x20.ContactType;
import net.opengis.ows.x20.OnlineResourceType;
import net.opengis.ows.x20.ResponsiblePartySubsetType;
import net.opengis.ows.x20.ServiceIdentificationDocument;
import net.opengis.ows.x20.ServiceProviderDocument.ServiceProvider;
import net.opengis.wps.x20.CapabilitiesDocument;
import net.opengis.wps.x20.ContentsDocument;
import net.opengis.wps.x20.ProcessSummaryType;
import net.opengis.wps.x20.WPSCapabilitiesType;
import org.n52.restfulwpsproxy.util.EndpointUtil;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class WPSCapabilitiesJsonModule extends AbstractWPSJsonModule {

    private static final String _VERSION = "_version";
    private static final String _SERVICE = "_service";
    private static final String CONTENTS = "Contents";
    private static final String SERVICE_PROVIDER = "ServiceProvider";
    private static final String SERVICE_IDENTIFICATION = "ServiceIdentification";
    private static final String CAPABILITIES = "Capabilities";
    private static final String ACCESS_CONSTRAINTS = "AccessConstraints";
    private static final String FEES = "Fees";
    private static final String SERVICE_TYPE_VERSION = "ServiceTypeVersion";
    private static final String SERVICE_TYPE = "ServiceType";
    private static final String A_ABSTRACT = "Abstract";
    private static final String TITLE = "Title";
    private static final String SERVICE_CONTACT = "ServiceContact";
    private static final String PROVIDER_SITE = "ProviderSite";
    private static final String PROVIDER_NAME = "ProviderName";
    private static final String ARCROLE = "Arcrole";
    private static final String ROLE = "Role";
    private static final String H_REF = "HRef";
    private static final String HOURS_OF_SERVICE = "HoursOfService";
    private static final String CONTACT_INSTRUCTIONS = "ContactInstructions";
    private static final String CONTACT_INFO = "ContactInfo";
    private static final String INDIVIDUAL_NAME = "IndividualName";
    private static final String ELECTRONIC_MAIL_ADDRESS = "ElectronicMailAddress";
    private static final String COUNTRY = "Country";
    private static final String PORTAL_CODE = "PortalCode";
    private static final String ADMINISTRATIVE_AREA = "AdministrativeArea";
    private static final String CITY = "City";
    private static final String DELIVERY_POINT = "DeliveryPoint";
    private static final String URL = "url";
    private static final String _OUTPUT_TRANSMISSION = "_outputTransmission";
    private static final String _JOB_CONTROL_OPTIONS = "_jobControlOptions";
    private static final String _PROCESS_VERSION = "_processVersion";
    private static final String TITLE1 = "title";
    private static final String IDENTIFIER = "identifier";
    private static final String PROCESS_SUMMARIES = "ProcessSummaries";

    /**
     * Default Constructor.
     */
    public WPSCapabilitiesJsonModule() {
        addSerializer(new CapabilitiesJsonEncoder());
        addSerializer(new ServiceIdentificationEncoder());
        addSerializer(new ServiceProviderEncoder());
        addSerializer(new OnlineResourceSerializer());
        addSerializer(new ServiceContactSerializer());
        addSerializer(new ContactTypeSerializer());
        addSerializer(new AddressJsonSerializer());
        addSerializer(new ContentsJsonSerializer());
        addSerializer(new ProcessSummarySerializer());
    }

    private static final class CapabilitiesJsonEncoder extends JsonSerializer<CapabilitiesDocument> {

        @Override
        public void serialize(CapabilitiesDocument cp, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            WPSCapabilitiesType capabilities = cp.getCapabilities();
            jg.writeStartObject();
            jg.writeObjectFieldStart(CAPABILITIES);
            jg.writeObjectField(SERVICE_IDENTIFICATION, capabilities.getServiceIdentification());
            jg.writeObjectField(SERVICE_PROVIDER, capabilities.getServiceProvider());
            jg.writeObjectField(CONTENTS, capabilities.getContents());
            jg.writeStringField(_SERVICE, capabilities.getService().getStringValue());
            jg.writeStringField(_VERSION, capabilities.getVersion());
            jg.writeEndObject();
            jg.writeEndObject();
        }

        @Override
        public Class<CapabilitiesDocument> handledType() {
            return CapabilitiesDocument.class;
        }
    }

    private static final class ServiceIdentificationEncoder
            extends JsonSerializer<ServiceIdentificationDocument.ServiceIdentification> {

        @Override
        public void serialize(ServiceIdentificationDocument.ServiceIdentification si,
                JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            writeStringFieldIfNotNull(jg, TITLE, si.getTitleArray(0).getStringValue());
            writeStringFieldIfNotNull(jg, A_ABSTRACT, si.getAbstractArray(0).getStringValue());
            jg.writeStringField(SERVICE_TYPE, si.getServiceType().getStringValue());
            jg.writeStringField(SERVICE_TYPE_VERSION, si.getServiceTypeVersionArray(0));
            writeStringFieldIfNotNull(jg, FEES, si.getFees());
            writeStringFieldIfNotNull(jg, ACCESS_CONSTRAINTS, si.getAccessConstraintsArray(0));
            jg.writeEndObject();
        }

        @Override
        public Class<ServiceIdentificationDocument.ServiceIdentification> handledType() {
            return ServiceIdentificationDocument.ServiceIdentification.class;
        }
    }

    private static final class ServiceProviderEncoder extends JsonSerializer<ServiceProvider> {

        @Override
        public void serialize(ServiceProvider t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(PROVIDER_NAME, t.getProviderName());
            jg.writeObjectField(PROVIDER_SITE, t.getProviderSite());
            jg.writeObjectField(SERVICE_CONTACT, t.getServiceContact());
            jg.writeEndObject();
        }

        @Override
        public Class<ServiceProvider> handledType() {
            return ServiceProvider.class;
        }
    }

    private static final class OnlineResourceSerializer extends JsonSerializer<OnlineResourceType> {

        @Override
        public void serialize(OnlineResourceType t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            writeStringFieldIfNotNull(jg, TITLE, t.getTitle());
            writeStringFieldIfNotNull(jg, H_REF, t.getHref());
            writeStringFieldIfNotNull(jg, ROLE, t.getRole());
            writeStringFieldIfNotNull(jg, ARCROLE, t.getArcrole());
            jg.writeEndObject();
        }

        @Override
        public Class<OnlineResourceType> handledType() {
            return OnlineResourceType.class;
        }

    }

    private static final class ServiceContactSerializer extends JsonSerializer<ResponsiblePartySubsetType> {

        @Override
        public void serialize(ResponsiblePartySubsetType t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(INDIVIDUAL_NAME, t.getIndividualName());
            jg.writeObjectField(CONTACT_INFO, t.getContactInfo());
            jg.writeEndObject();
        }

        @Override
        public Class<ResponsiblePartySubsetType> handledType() {
            return ResponsiblePartySubsetType.class;
        }
    }

    private static final class ContactTypeSerializer extends JsonSerializer<ContactType> {

        @Override
        public void serialize(ContactType t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            writeStringFieldIfNotNull(jg, CONTACT_INSTRUCTIONS, t.getContactInstructions());
            writeStringFieldIfNotNull(jg, HOURS_OF_SERVICE, t.getHoursOfService());
            // TODO add others
            jg.writeEndObject();
        }

        @Override
        public Class<ContactType> handledType() {
            return ContactType.class;
        }

    }

    private static final class AddressJsonSerializer extends JsonSerializer<AddressType> {

        @Override
        public void serialize(AddressType t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            writeStringFieldIfNotNull(jg, DELIVERY_POINT, t.getDeliveryPointArray(0));
            writeStringFieldIfNotNull(jg, CITY, t.getCity());
            writeStringFieldIfNotNull(jg, ADMINISTRATIVE_AREA, t.getAdministrativeArea());
            writeStringFieldIfNotNull(jg, PORTAL_CODE, t.getPostalCode());
            writeStringFieldIfNotNull(jg, COUNTRY, t.getCountry());
            writeStringFieldIfNotNull(jg, ELECTRONIC_MAIL_ADDRESS, t.getElectronicMailAddressArray(0));
            jg.writeEndObject();
        }

        @Override
        public Class<AddressType> handledType() {
            return AddressType.class;
        }
    }

    private static final class ContentsJsonSerializer extends JsonSerializer<ContentsDocument.Contents> {

        @Override
        public void serialize(ContentsDocument.Contents t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeArrayFieldStart(PROCESS_SUMMARIES);
            ProcessSummaryType[] processSummaryArray = t.getProcessSummaryArray();
            for (ProcessSummaryType pst : processSummaryArray) {
                jg.writeObject(pst);
            }
            jg.writeEndArray();
            jg.writeEndObject();
        }

        @Override
        public Class<ContentsDocument.Contents> handledType() {
            return ContentsDocument.Contents.class;
        }
    }

    private static final class ProcessSummarySerializer extends JsonSerializer<ProcessSummaryType> {

        @Override
        public void serialize(ProcessSummaryType t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(IDENTIFIER, t.getIdentifier().getStringValue());
            jg.writeStringField(TITLE1, t.getTitleArray(0).getStringValue());
            jg.writeStringField(_PROCESS_VERSION, t.getProcessVersion());
            jg.writeStringField(_JOB_CONTROL_OPTIONS, t.getJobControlOptions().get(0).toString());
            jg.writeStringField(_OUTPUT_TRANSMISSION, t.getOutputTransmission().get(0).toString());
            jg.writeStringField(URL, EndpointUtil.PROXYBASEURL + "processes/" + t.getIdentifier().getStringValue());
            jg.writeEndObject();
        }

        @Override
        public Class<ProcessSummaryType> handledType() {
            return ProcessSummaryType.class; //To change body of generated methods, choose Tools | Templates.
        }
    }
}
