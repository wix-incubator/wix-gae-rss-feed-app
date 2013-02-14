package com.wixpress.testapp.dao;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public interface SampleAppDao {

    public AppSettings addAppInstance(AppSettings appSettings, UUID instanceId);

    public AppSettings getAppInstance(UUID instanceId);

    public void update(AppSettings appInstance, UUID instanceId);

}
