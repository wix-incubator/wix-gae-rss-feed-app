package com.wixpress.app.dao;

/**
 * Data store wrapper
 * It implements methods for getting and setting data in the data store
 */

public interface SampleAppDao {

    /**
     * Save app settings in the data store
     * @param instanceId - Instance id of the app, It is shared by multiple Widgets of the same app within the same site
     * @param compId - The ID of the Wix component which is the host of the iFrame, it is used to distinguish between multiple instances of the same Widget in a site
     * @param appSettings - The settings of the app that configure the widget
     */
    public void saveAppSettings(String instanceId, String compId, AppSettings appSettings);

    /**
     * Get app settings from the data store
     * @param instanceId - Instance id of the app, It is shared by multiple Widgets of the same app within the same site
     * @param compId - The ID of the Wix component which is the host of the iFrame, it is used to distinguish between multiple instances of the same Widget in a site
     * @return appSettings - The settings of the app that configure the widget
     */
    public AppSettings getAppSettings(String instanceId, String compId);

    /**
     * Update app settings in the data store
     * @param instanceId - Instance id of the app, It is shared by multiple Widgets of the same app within the same site
     * @param compId - The ID of the Wix component which is the host of the iFrame, it is used to distinguish between multiple instances of the same Widget in a site
     * @param appSettings - The settings of the app that configure the widget
     */
    public void updateAppSettings(String instanceId, String compId, AppSettings appSettings);
}
