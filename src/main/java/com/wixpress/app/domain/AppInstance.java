package com.wixpress.app.domain;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * This class represent the app instance properties
 */

public class AppInstance {

    private UUID instanceId; // The instance ID of the app within Wix
    private DateTime signDate;
    private UUID uid; // The ID of the site-member that is currently logged in (optional)
    private String permissions;// The permission set of the site member.
    private String ipAndPort;
    private String vendorProductId; // Premium Package ID, as was entered in the Dev Center during the app registration process
    private Boolean demoMode;

    //Empty cont' for the ObjectMapper - don't delete
    public AppInstance() {
    }

    public AppInstance(UUID instanceId) {
        this.instanceId = instanceId;
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

    public String getIpAndPort() {
        return ipAndPort;
    }

    public void setIpAndPort(String ipAndPort) {
        this.ipAndPort = ipAndPort;
    }

    public String getVendorProductId() {
        return vendorProductId;
    }

    public void setVendorProductId(String vendorProductId) {
        this.vendorProductId = vendorProductId;
    }

    public Boolean getDemoMode() {
        return demoMode;
    }

    public void setDemoMode(Boolean demoMode) {
        this.demoMode = demoMode;
    }
}
