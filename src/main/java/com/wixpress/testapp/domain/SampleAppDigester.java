package com.wixpress.testapp.domain;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by : doron
 * Since: 8/27/12
 */
public class SampleAppDigester {

    ObjectMapper objectMapper = new ObjectMapper();

    public String serializeSampleAppInstance(SampleAppInstance sampleAppInstance) {
        try {
            return objectMapper.writeValueAsString(sampleAppInstance);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SampleAppInstance deserializeSampleAppInstance(String sampleAppInstanceJson) {
        try {
            return objectMapper.readValue(sampleAppInstanceJson, SampleAppInstance.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
