package com.wixpress.app.dao;

import org.codehaus.jackson.annotate.JsonTypeName;

import javax.annotation.Nullable;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@JsonTypeName("AppSettings")
public class AppSettings {

    private @Nullable String textColor = "#000000";
    private @Nullable String titleTextColor = "#000000";
    private @Nullable String widgetBcgColor = "#FFF";
    private @Nullable Boolean widgetBcgCB = false;
    private @Nullable Double widgetBcgSlider = 0.5;
    private @Nullable String feedBcgColor = "#F8F8F8";
    private @Nullable Boolean feedBcgCB = false;
    private @Nullable Double feedBcgSlider = 0.5;
    private @Nullable String feedInputUrl = "";
    private @Nullable Integer numOfEntries = 4;

    public AppSettings() {}

    public AppSettings updateFromInput(AppSettings updated) {
        AppSettings newAppSettings = new AppSettings();
        newAppSettings.textColor = nvl(updated.textColor, textColor);
        newAppSettings.titleTextColor = nvl(updated.titleTextColor, titleTextColor);
        newAppSettings.widgetBcgColor = nvl(updated.widgetBcgColor, widgetBcgColor);
        newAppSettings.widgetBcgCB = nvl(updated.widgetBcgCB, widgetBcgCB);
        newAppSettings.widgetBcgSlider = nvl(updated.widgetBcgSlider, widgetBcgSlider);
        newAppSettings.feedBcgColor = nvl(updated.feedBcgColor, feedBcgColor);
        newAppSettings.feedBcgCB = nvl(updated.feedBcgCB, feedBcgCB);
        newAppSettings.feedBcgSlider = nvl(updated.feedBcgSlider, feedBcgSlider);
        newAppSettings.feedInputUrl = nvl(updated.feedInputUrl, feedInputUrl);
        newAppSettings.numOfEntries = nvl(updated.numOfEntries, numOfEntries);
        return newAppSettings;
    }

    public <T> T nvl(T value, T fallback) {
        return (value != null)?value:fallback;
    }

    @Nullable
    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(@Nullable String textColor) {
        this.textColor = textColor;
    }

    @Nullable
    public String getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(@Nullable String titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    @Nullable
    public String getWidgetBcgColor() {
        return widgetBcgColor;
    }

    public void setWidgetBcgColor(@Nullable String widgetBcgColor) {
        this.widgetBcgColor = widgetBcgColor;
    }

    @Nullable
    public Boolean getWidgetBcgCB() {
        return widgetBcgCB;
    }

    public void setWidgetBcgCB(@Nullable Boolean widgetBcgCB) {
        this.widgetBcgCB = widgetBcgCB;
    }

    @Nullable
    public Double getWidgetBcgSlider() {
        return widgetBcgSlider;
    }

    public void setWidgetBcgSlider(@Nullable Double widgetBcgSlider) {
        this.widgetBcgSlider = widgetBcgSlider;
    }

    @Nullable
    public String getFeedBcgColor() {
        return feedBcgColor;
    }

    public void setFeedBcgColor(@Nullable String feedBcgColor) {
        this.feedBcgColor = feedBcgColor;
    }

    @Nullable
    public Boolean getFeedBcgCB() {
        return feedBcgCB;
    }

    public void setFeedBcgCB(@Nullable Boolean feedBcgCB) {
        this.feedBcgCB = feedBcgCB;
    }

    @Nullable
    public Double getFeedBcgSlider() {
        return feedBcgSlider;
    }

    public void setFeedBcgSlider(@Nullable Double feedBcgSlider) {
        this.feedBcgSlider = feedBcgSlider;
    }

    @Nullable
    public String getFeedInputUrl() {
        return feedInputUrl;
    }

    public void setFeedInputUrl(@Nullable String feedInputUrl) {
        this.feedInputUrl = feedInputUrl;
    }

    @Nullable
    public Integer getNumOfEntries() {
        return numOfEntries;
    }

    public void setNumOfEntries(@Nullable Integer numOfEntries) {
        this.numOfEntries = numOfEntries;
    }
}

