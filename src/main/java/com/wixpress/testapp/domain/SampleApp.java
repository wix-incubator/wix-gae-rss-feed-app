package com.wixpress.testapp.domain;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public class SampleApp {

    public final static String EndpointControllerUrl = "/test-app";
    private final static String DEFAULT_APPLICATION_ID = "129a90ff-094d-f193-49a0-2da5d7d2209b";
    private final static String DEFAULT_SECRET_ID = "39202616-8cfc-4a28-a8d7-4790d13de94e";
    private final static Integer MAX_NUMBER_OF_INSTANCES = 20;

    private String applicationID;
    private String applicationSecret;

    //Instead of DB..
//    private List<SampleAppInstance> sampleAppInstanceMap = newArrayList();

    private LRUCache<UUID, SampleAppInstance> sampleAppInstanceMap = new LRUCache<UUID, SampleAppInstance>(MAX_NUMBER_OF_INSTANCES);


    public SampleApp() {
        this.applicationID = DEFAULT_APPLICATION_ID;
        this.applicationSecret = DEFAULT_SECRET_ID;
    }

    public SampleApp(String applicationID, String applicationSecret) {
        this.applicationID = applicationID;
        this.applicationSecret = applicationSecret;
    }

    public void addAppInstance(SampleAppInstance sampleAppInstance) {
        sampleAppInstanceMap.put(sampleAppInstance.getInstanceId(), sampleAppInstance);
    }

    public SampleAppInstance addAppInstance(WixSignedInstance wixSignedInstance) {
        SampleAppInstance sampleAppInstance = new SampleAppInstance(wixSignedInstance);
        sampleAppInstanceMap.put(sampleAppInstance.getInstanceId(), sampleAppInstance);
        return sampleAppInstance;
    }

    public SampleAppInstance getAppInstance(WixSignedInstance wixSignedInstance) {
        return getAppInstance(wixSignedInstance.getInstanceId());
    }

    public SampleAppInstance getAppInstance(UUID instanceId) {
        if(instanceId == null)
            return null;
        else
            return sampleAppInstanceMap.get(instanceId);
    }

    public String getApplicationID() {
        return applicationID;
    }

    public String getApplicationSecret() {
        return applicationSecret;
    }

    public LRUCache<UUID, SampleAppInstance> getSampleAppInstanceMap() {
        return sampleAppInstanceMap;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public void setApplicationSecret(String applicationSecret) {
        this.applicationSecret = applicationSecret;
    }
}
