package com.wixpress.app.dao;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public interface SampleAppDao {

    public AppSettings saveAppSettings(AppSettings appSettings, UUID instanceId, String compId);

    public AppSettings getAppSettings(UUID instanceId, String compId);

    public void updateAppSettings(AppSettings appInstance, UUID instanceId, String compId);

}
