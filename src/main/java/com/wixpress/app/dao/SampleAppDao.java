package com.wixpress.app.dao;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public interface SampleAppDao {

    public AppSettings addAppInstance(AppSettings appSettings, UUID instanceId, String compId);

    public AppSettings getAppInstance(UUID instanceId, String compId);

    public void update(AppSettings appInstance, UUID instanceId, String compId);

}
