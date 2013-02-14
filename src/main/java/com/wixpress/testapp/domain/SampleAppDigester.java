package com.wixpress.testapp.domain;

import com.wixpress.testapp.dao.AppSettings;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by : doron
 * Since: 8/27/12
 */
public class SampleAppDigester {

    ObjectMapper objectMapper = new ObjectMapper();

    public String serializeSampleAppInstance(AppSettings appSettings) {
        try {
            return objectMapper.writeValueAsString(appSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AppSettings deserializeSampleAppInstance(String sampleAppInstanceJson) {
        try {
            return objectMapper.readValue(sampleAppInstanceJson, AppSettings.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
