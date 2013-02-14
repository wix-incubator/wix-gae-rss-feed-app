package com.wixpress.testapp.domain;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public class SampleApp {

    // todo - HardCoded defaults for staging environments - should be changed once you register your application with Wix
    protected final static String APPLICATION_ID = "12e04a9c-a51b-51d3-ccfd-44030f30f96a";
    protected final static String APPLICATION_SECRET = "7472876f-89eb-4f19-8f64-6f5415db5700";

    protected String applicationID;
    protected String applicationSecret;

    public SampleApp() {
        this.applicationID = APPLICATION_ID;
        this.applicationSecret = APPLICATION_SECRET;
    }

    public SampleApp(String applicationID, String applicationSecret) {
        this.applicationID = applicationID;
        this.applicationSecret = applicationSecret;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public String getApplicationSecret() {
        return applicationSecret;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public void setApplicationSecret(String applicationSecret) {
        this.applicationSecret = applicationSecret;
    }
}
