package com.wixpress.app.spring;

import com.google.common.collect.ImmutableMap;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import java.io.IOException;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@Configuration
public class EmbeddedAppVelocityBeansConfig
{
    @Bean
    public FileResourceLoader fileResourceLoader()
    {
        return new FileResourceLoader();
    }

    @Bean
    public VelocityEngine velocityEngine() throws IOException
    {
        VelocityEngineFactoryBean veFactory = new VelocityEngineFactoryBean();
        veFactory.setVelocityPropertiesMap(ImmutableMap.<String, Object>builder()
                .put("input.encoding", "UTF-8")
                .put("output.encoding", "UTF-8")
                .put("resource.loader", "sources,classpath")
                .put("classpath.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
                .put("classpath.resource.loader.path", "/")
                .put("sources.resource.loader.instance", fileResourceLoader())
                .put("resource.manager.defaultcache.size", 200)
                .put("sources.resource.loader.modificationCheckInterval", "0")
                .put("classpath.resource.loader.modificationCheckInterval", "0")
                .put("velocimacro.library", "org/springframework/web/servlet/view/velocity/spring.vm")
                .build()
        );
        veFactory.afterPropertiesSet();
        return veFactory.getObject();
    }

    @Bean
    public VelocityConfigurer velocityConfigurer(VelocityEngine velocityEngine) throws IOException {
        VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
        velocityConfigurer.setVelocityEngine(velocityEngine);
        return velocityConfigurer;
    }

    @Bean
    public VelocityViewResolver viewResolver() {
        VelocityViewResolver resolver = new VelocityViewResolver();
        resolver.setCache(false);
        resolver.setPrefix("views/");
        resolver.setSuffix(".vm");
        resolver.setExposeSpringMacroHelpers(true);
        resolver.setExposeRequestAttributes(true);
        resolver.setContentType("text/html;charset=UTF-8");
        return resolver;
    }
}
