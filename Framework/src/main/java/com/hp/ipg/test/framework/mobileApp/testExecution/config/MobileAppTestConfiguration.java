package com.hp.ipg.test.framework.mobileApp.testExecution.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = { "com.hp.ipg.test.framework.mobileApp.testExecution.config", "com.hp.ipg.test.framework.genericLib" })
public class MobileAppTestConfiguration {

    @Configuration
    @PropertySource("classpath:properties/mobileAppTest/qa.properties")
    @Profile({ "qa" })
    static class QA {
    }

    @Configuration
    @PropertySource("classpath:properties/mobileAppTest/dev.properties")
    @Profile({ "dev" })
    static class Dev {
    }

    @Value("${hpRoam.email}")
    private String email;

    @Value("${hpRoam.password}")
    private String password;

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        pspc.setLocalOverride(true);
        return pspc;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

}
