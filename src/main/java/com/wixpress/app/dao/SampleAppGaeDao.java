package com.wixpress.app.dao;

import com.google.appengine.api.datastore.*;
import org.codehaus.jackson.map.ObjectMapper;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by : doron
 * Since: 8/27/12
 */

public class SampleAppGaeDao implements SampleAppDao {

    protected final static String SAMPLE_APP_INSTANCE = "AppSettings";
    protected final static String BAGGAGE = "baggage";

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private DatastoreService dataStore;

    public AppSettings saveAppSettings(AppSettings appSettings, UUID instanceId, String compId) {
        Entity entity = new Entity(SAMPLE_APP_INSTANCE, key(instanceId, compId));
        try {
            entity.setProperty(BAGGAGE, objectMapper.writeValueAsString(appSettings));
        } catch (IOException e) {
            throw new SampleAppDaoException("failed to serialize settings", e);
        }

        Transaction transaction = dataStore.beginTransaction();
        try {
            dataStore.put(entity);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return appSettings;
    }

    public @Nullable AppSettings getAppSettings(UUID instanceId, String compId) {
        if (instanceId == null)
            return null;
        else {
            final Key key = KeyFactory.createKey(SAMPLE_APP_INSTANCE, key(instanceId, compId));
            try {
                final String baggage = dataStore.get(key).getProperty(BAGGAGE).toString();
                return objectMapper.readValue(baggage, AppSettings.class);
            } catch (EntityNotFoundException e) {
                // we ignore the setting reading exception and return a new default settings object
                return new AppSettings();
            } catch (IOException e) {
                // we ignore the setting reading exception and return a new default settings object
                return new AppSettings();
            }
        }
    }

    public String key(UUID instanceId, String compId) {
        return String.format("%s.%s", instanceId.toString(), compId);
    }

    public void updateAppSettings(AppSettings appSettings, UUID instanceId, String compId) {
        saveAppSettings(appSettings, instanceId, compId);
    }
}