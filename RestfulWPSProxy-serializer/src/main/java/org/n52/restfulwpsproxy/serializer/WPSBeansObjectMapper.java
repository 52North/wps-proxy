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

import com.fasterxml.jackson.annotation.JsonInclude;
import org.n52.restfulwpsproxy.serializer.json.WPSCapabilitiesJsonModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.n52.restfulwpsproxy.serializer.json.WPSProcessesJsonModule;
import org.n52.restfulwpsproxy.serializer.json.WPSStatusJsonModule;
import org.n52.restfulwpsproxy.serializer.json.WPSExecuteJsonModule;
import org.n52.restfulwpsproxy.serializer.json.WPSGetResultsJsonModule;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class WPSBeansObjectMapper extends ObjectMapper {
    
    public WPSBeansObjectMapper() {
        this.registerModule(new WPSCapabilitiesJsonModule());
        this.registerModule(new WPSProcessesJsonModule());
        this.registerModule(new WPSStatusJsonModule());
        this.registerModule(new WPSExecuteJsonModule(this));
        this.registerModule(new WPSGetResultsJsonModule());
        
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    }
}
