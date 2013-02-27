package com.wixpress.app.dao;

import com.google.appengine.api.datastore.*;
import org.codehaus.jackson.map.ObjectMapper;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * The DB wrapper of the app
 * It implements methods for getting and setting data in the DB
 */

public class SampleAppGaeDao implements SampleAppDao {

    protected final static String SAMPLE_APP_INSTANCE = "RssFeedAppInstance";
    protected final static String BAGGAGE = "baggage";

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private DatastoreService dataStore;

    /**
     * Save app settings in the DB
     * @param instanceId - Instance id of the app, It is shared by multiple Widgets of the same app within the same site
     * @param compId - The ID of the Wix component which is the host of the iFrame, it is used to distinguish between multiple instances of the same Widget in a site
     * @param appSettings - The settings of the app that configure the widget
     */
    public void saveAppSettings(String instanceId, String compId, AppSettings appSettings) {
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
    }

    /**
     * Get app settings from the DB
     * @param instanceId - Instance id of the app, It is shared by multiple Widgets of the same app within the same site
     * @param compId - The ID of the Wix component which is the host of the iFrame, it is used to distinguish between multiple instances of the same Widget in a site
     * @return
     */
    public @Nullable AppSettings getAppSettings(String instanceId, String compId) {
        if (instanceId == null)
            return null;
        else {
            final Key key = KeyFactory.createKey(SAMPLE_APP_INSTANCE, key(instanceId, compId));
            try {
                final String baggage = dataStore.get(key).getProperty(BAGGAGE).toString();
                return objectMapper.readValue(baggage, AppSettings.class);
            } catch (EntityNotFoundException e) {
                // we ignore the setting reading exception and return a new default settings object
                return new AppSettings(objectMapper);
            } catch (IOException e) {
                // we ignore the setting reading exception and return a new default settings object
                return new AppSettings(objectMapper);
            }
        }
    }

    /**
     * Create a unique key to each entry in the DB that is composed from the instanceId and compID
     * @param instanceId - Instance id of the app, It is shared by multiple Widgets of the same app within the same site
     * @param compId - The ID of the Wix component which is the host of the iFrame, it is used to distinguish between multiple instances of the same Widget in a site
     * @return
     */
    public String key(String instanceId, String compId) {
        return String.format("%s.%s", instanceId, compId);
    }

    /**
     * Update app settings in the DB
     * @param instanceId - Instance id of the app, It is shared by multiple Widgets of the same app within the same site
     * @param compId - The ID of the Wix component which is the host of the iFrame, it is used to distinguish between multiple instances of the same Widget in a site
     * @param appSettings - The settings of the app that configure the widget
     */
    public void updateAppSettings(String instanceId, String compId, AppSettings appSettings) {
        saveAppSettings(instanceId, compId, appSettings);
    }
}
