package com.wixpress.testapp.domain;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public class SampleAppInstance {

    private String title = "My App";
    private String color = "white";
    private Integer height = 500;

    private Integer width = 500;
    private UUID instanceId;
    private DateTime signDate;
    private DateTime lastAccessedDate;
    private UUID uid;
    private String permissions = "";

    public SampleAppInstance(WixSignedInstance wixSignedInstance) {
        this.instanceId = wixSignedInstance.getInstanceId();
        this.signDate = wixSignedInstance.getSignDate();
        this.lastAccessedDate = wixSignedInstance.getSignDate();
        this.uid = wixSignedInstance.getUid();
        this.permissions = wixSignedInstance.getPermissions();
    }

    public DateTime getLastAccessedDate() {
        return lastAccessedDate;
    }

    public void setLastAccessedDate(DateTime lastAccessedDate) {
        this.lastAccessedDate = lastAccessedDate;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
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

    public UUID getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(UUID instanceId) {
        this.instanceId = instanceId;
    }

    public DateTime getSignDate() {
        return signDate;
    }

    public void setSignDate(DateTime signDate) {
        this.signDate = signDate;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public void update(String color, String title) {
        this.color = color;
        this.title = title;
    }
}
