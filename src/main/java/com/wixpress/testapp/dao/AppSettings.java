package com.wixpress.testapp.dao;

import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@JsonTypeName("AppSettings")
public class AppSettings {

    private String title = "My App";
    private String color = "lavender";

    public AppSettings() {}

    public AppSettings(String title, String color) {

        this.title = title;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
