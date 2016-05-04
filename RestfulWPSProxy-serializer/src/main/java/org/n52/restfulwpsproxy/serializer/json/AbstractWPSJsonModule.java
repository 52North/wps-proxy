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
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author adewa
 */
public abstract class AbstractWPSJsonModule extends SimpleModule {

    protected String getFirstArrayElementAsStringIfExist(List arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            return arrayList.get(0).toString();
        }
        return null;
    }

    protected static final void writeArrayOfObjects(String fieldName, Object[] objects, JsonGenerator jg) throws IOException {
        jg.writeArrayFieldStart(fieldName);
        for (Object o : objects) {
            jg.writeObject(o);
        }
        jg.writeEndArray();
    }
    
    protected static final String toStringOrEmpty(Object object){
        return object == null ? "" : object.toString();
    }
    
    protected static final String toStringOrNull(Object object){
        return object == null ? null : object.toString();
    }
}
