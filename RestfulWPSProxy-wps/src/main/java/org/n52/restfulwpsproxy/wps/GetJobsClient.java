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
package org.n52.restfulwpsproxy.wps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
public class GetJobsClient extends AbstractWPSClient {

	private Map<String, List<String>> processIdJobIdsMap;

    /**
     * Constructor.
     *
     * @param baseUrl baseUrl des WPS services.
     * @param restTemplate
     */
    public GetJobsClient(String baseUrl, RestTemplate restTemplate) {
        super(baseUrl, restTemplate);
        processIdJobIdsMap = new HashMap<>();
    }

    public String[] getJobIds(String processId) {

    	List<String> processIdList = processIdJobIdsMap.get(processId);
    	
    	String[] processIdArray = new String[]{};
    	
    	if(processIdList != null){
    		processIdArray = processIdList.toArray(processIdArray);
    	}
    	
        return processIdArray;
    }
    
    public void addJobId(String processId, String jobId){
    	if(processIdJobIdsMap.containsKey(processId)){
    		processIdJobIdsMap.get(processId).add(jobId);
    	}else{
    		List<String> newJobIdList = new ArrayList<>();
    		newJobIdList.add(jobId);
    		processIdJobIdsMap.put(processId, newJobIdList);
    	}
    }

}
