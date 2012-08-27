package com.wixpress.testapp.domain;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 8/27/12
 */

public class SampleAppLRU extends SampleApp {

    protected final static Integer MAX_NUMBER_OF_INSTANCES = 20;

    // Instead of DB..
    private LRUCache<UUID, SampleAppInstance> sampleAppInstanceMap = new LRUCache<UUID, SampleAppInstance>(MAX_NUMBER_OF_INSTANCES);

    @Override
    public SampleAppInstance addAppInstance(SampleAppInstance sampleAppInstance) {
        return sampleAppInstanceMap.put(sampleAppInstance.getInstanceId(), sampleAppInstance);
    }

    @Override
    public SampleAppInstance addAppInstance(WixSignedInstance wixSignedInstance) {
        SampleAppInstance sampleAppInstance = new SampleAppInstance(wixSignedInstance);
        sampleAppInstanceMap.put(sampleAppInstance.getInstanceId(), sampleAppInstance);
        return sampleAppInstance;
    }

    @Override
    public SampleAppInstance getAppInstance(WixSignedInstance wixSignedInstance) {
        return getAppInstance(wixSignedInstance.getInstanceId());
    }

    @Override
    public SampleAppInstance getAppInstance(UUID instanceId) {
        if (instanceId == null)
            return null;
        else
            return sampleAppInstanceMap.get(instanceId);
    }

    @Override
    public void update(SampleAppInstance appInstance) {
        //TODO
    }

    public LRUCache<UUID, SampleAppInstance> getSampleAppInstanceMap() {
        return sampleAppInstanceMap;
    }
}
