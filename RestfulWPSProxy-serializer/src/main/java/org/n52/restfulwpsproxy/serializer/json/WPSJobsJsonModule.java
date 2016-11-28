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

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * TODO JavaDoc
 *
 * @author bpross-52n
 */
public class WPSJobsJsonModule extends AbstractWPSJsonModule {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5430054342100585697L;
	private static final String JOBS = "Jobs";

    /**
     * Default Constructor.
     */
    public WPSJobsJsonModule() {
        addSerializer(new JobsSerializer());
    }

    private static final class JobsSerializer extends JsonSerializer<String[]> {

        @Override
        public void serialize(String[] t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        	jg.writeStartObject();
        	writeArrayOfStrings(JOBS, t, jg);
        	jg.writeEndObject();
        }

        @Override
        public Class<String[]> handledType() {
            return String[].class;
        }
    }
}
