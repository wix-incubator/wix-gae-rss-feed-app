package com.wixpress.testapp.dao;

import com.wixpress.testapp.domain.LRUCache;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 8/27/12
 */

public class SampleAppLruDao implements SampleAppDao {

    protected final static Integer MAX_NUMBER_OF_INSTANCES = 20;

    // Instead of DB..
    private LRUCache<UUID, AppSettings> sampleAppInstanceMap = new LRUCache<UUID, AppSettings>(MAX_NUMBER_OF_INSTANCES);

    @Override
    public AppSettings addAppInstance(AppSettings appSettings, UUID instanceId) {
        return sampleAppInstanceMap.put(instanceId, appSettings);
    }

    @Override
    public AppSettings getAppInstance(UUID instanceId) {
        if (instanceId == null)
            return null;
        else
            return sampleAppInstanceMap.get(instanceId);
    }

    @Override
    public void update(AppSettings appSettings, UUID instanceId) {
        sampleAppInstanceMap.put(instanceId, appSettings);
    }

    public LRUCache<UUID, AppSettings> getSampleAppInstanceMap() {
        return sampleAppInstanceMap;
    }
}
