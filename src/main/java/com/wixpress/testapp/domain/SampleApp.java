package com.wixpress.testapp.domain;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public abstract class SampleApp {

    // todo - HardCoded defaults for staging environments - should be changed once you register your application with Wix
    protected final static String DEFAULT_APPLICATION_ID = "129a90ff-094d-f193-49a0-2da5d7d2209b";
    protected final static String DEFAULT_SECRET_ID = "39202616-8cfc-4a28-a8d7-4790d13de94e";

    protected final static String SAMPLE_APP_INSTANCE = "SampleAppInstance";
    protected final static String BAGGAGE = "baggage";

    protected String applicationID;
    protected String applicationSecret;

    public SampleApp() {
        this.applicationID = DEFAULT_APPLICATION_ID;
        this.applicationSecret = DEFAULT_SECRET_ID;
    }

    public SampleApp(String applicationID, String applicationSecret) {
        this.applicationID = applicationID;
        this.applicationSecret = applicationSecret;
    }

    public abstract SampleAppInstance addAppInstance(SampleAppInstance sampleAppInstance);

    public abstract SampleAppInstance addAppInstance(WixSignedInstance wixSignedInstance);

    public abstract SampleAppInstance getAppInstance(UUID instanceId);

    public abstract SampleAppInstance getAppInstance(WixSignedInstance wixSignedInstance);

    public abstract void update(SampleAppInstance appInstance);


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
