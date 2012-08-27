package com.wixpress.testapp.spring;

import com.wixpress.testapp.controller.SampleAppController;
import com.wixpress.testapp.domain.SampleApp;
import com.wixpress.testapp.domain.SampleAppDB;
import com.wixpress.testapp.domain.SampleAppLRU;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

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
    public SampleApp sampleApp() {
        return new SampleAppDB();
//        return new SampleAppLRU();
    }
}
