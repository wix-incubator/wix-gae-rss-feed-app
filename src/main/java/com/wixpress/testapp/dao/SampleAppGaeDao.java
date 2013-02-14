package com.wixpress.testapp.dao;

import com.google.appengine.api.datastore.*;
import com.wixpress.testapp.domain.SampleAppDigester;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 8/27/12
 */

public class SampleAppGaeDao implements SampleAppDao {

    protected final static String SAMPLE_APP_INSTANCE = "AppSettings";
    protected final static String BAGGAGE = "baggage";

    private SampleAppDigester digester = new SampleAppDigester();
    private DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

    public SampleAppGaeDao() {
        super();
    }

    public AppSettings addAppInstance(AppSettings appSettings, UUID instanceId) {
        Entity entity = new Entity(SAMPLE_APP_INSTANCE, instanceId.toString());
        entity.setProperty(BAGGAGE, digester.serializeSampleAppInstance(appSettings));

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

    public AppSettings getAppInstance(UUID instanceId) {
        if (instanceId == null)
            return null;
        else {
            final Key key = KeyFactory.createKey(SAMPLE_APP_INSTANCE, instanceId.toString());
            try {
                final String baggage = dataStore.get(key).getProperty(BAGGAGE).toString();
                return digester.deserializeSampleAppInstance(baggage);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void update(AppSettings appSettings, UUID instanceId) {
        addAppInstance(appSettings, instanceId);
    }
}
