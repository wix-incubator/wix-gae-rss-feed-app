package com.wixpress.app.spring;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.wixpress.app.controller.HelpController;
import com.wixpress.app.controller.SampleAppController;
import com.wixpress.app.dao.SampleAppDao;
import com.wixpress.app.dao.SampleAppGaeDao;
import com.wixpress.app.domain.AuthenticationResolver;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@Configuration
@Import({EmbeddedAppVelocityBeansConfig.class})
public class EmbeddedAppConfig
{
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SampleAppController sampleAppController() {
        return new SampleAppController();
    }

    @Bean
    public HelpController helloWorldController() {
        return new HelpController();
    }

    @Bean
    public SampleAppDao sampleAppDap() {
        return new SampleAppGaeDao();
    }

    @Bean
    public DatastoreService dataStore() {
        return DatastoreServiceFactory.getDatastoreService();
    }

    @Bean
    public AuthenticationResolver authenticationResolver() {
        return new AuthenticationResolver(objectMapper());
    }

}
