package com.hp.ipg.test.framework.webApp.testExecution.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * This is configuration class which loads properties file based on spring profile
 * and make it available to project.
 */
@Configuration
@Import({ WebAppTestConfiguration.class })
public class WebAppBaseTestConfiguration {
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        pspc.setLocalOverride(true);
        return pspc;
    }
}
