package com.hp.ipg.test.framework.webApp.testExecution.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = { "com.hp.ipg.test.framework.webApp.testExecution.config", "com.hp.ipg.test.framework.genericLib" })
public class WebAppTestConfiguration {

    @Configuration
    @PropertySource("classpath:properties/webAppTest/qa.properties")
    @Profile({ "qa" })
    static class QA {
    }

    @Configuration
    @PropertySource("classpath:properties/webAppTest/dev.properties")
    @Profile({ "dev" })
    static class Dev {
    }

    @Value("${roamUI.email}")
    private String email;

    @Value("${roamUI.password}")
    private String password;

    @Value("${roamUI.URL}")
    private String url;

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

    public String getUrl() { return this.url; }
}
