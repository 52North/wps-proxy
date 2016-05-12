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
