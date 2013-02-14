package com.wixpress.testapp.spring;

import com.wixpress.testapp.controller.HelpController;
import com.wixpress.testapp.controller.SampleAppController;
import com.wixpress.testapp.dao.SampleAppDao;
import com.wixpress.testapp.dao.SampleAppGaeDao;
import com.wixpress.testapp.domain.SampleApp;
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
    public SampleApp sampleApp() {
        return new SampleApp();
    }

    @Bean
    public SampleAppDao sampleAppDap() {
        return new SampleAppGaeDao();
    }
}
