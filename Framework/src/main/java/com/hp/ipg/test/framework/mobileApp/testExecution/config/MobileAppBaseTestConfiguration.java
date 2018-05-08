package com.hp.ipg.test.framework.mobileApp.testExecution.config;

import com.hp.ipg.test.framework.rsapi.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is configuration class which loads properties file based on spring profile
 * and make it available to project.
 */
@Configuration
@Import({ com.hp.ipg.test.framework.mobileApp.testExecution.config.MobileAppTestConfiguration.class })

public class MobileAppBaseTestConfiguration {

    public static final String TEST_ANY = "*";
    public static final String TEST_SEPARATOR = ",";
    public static final String TEST_PATH_SEPARATOR = ".";
    public static final String TEST_PATH_SEPARATOR_REGEX = "\\.";
    public static HashMap<String, ArrayList<String>> SKIP_TEST_CONFIG = new HashMap<>();

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        pspc.setLocalOverride(true);
        return pspc;
    }


}
