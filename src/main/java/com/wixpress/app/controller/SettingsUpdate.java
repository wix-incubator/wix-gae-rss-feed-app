package com.wixpress.app.controller;

import com.wixpress.app.dao.AppSettings;

/**
 * @author yoav
 * @since 2/17/13
 */
public class SettingsUpdate {
    private String compId;
    private AppSettings settings;

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public AppSettings getSettings() {
        return settings;
    }

    public void setSettings(AppSettings settings) {
        this.settings = settings;
    }
}
