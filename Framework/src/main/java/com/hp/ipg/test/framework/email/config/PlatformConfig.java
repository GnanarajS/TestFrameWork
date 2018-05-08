package com.hp.ipg.test.framework.email.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.hp.ipg.test.framework.email.EmailClientBase;
import com.hp.ipg.test.framework.email.MailinatorEmailClient;

@ComponentScan(basePackages = "com.hp.ipg.test.framework.email")
@Configuration
public class PlatformConfig {

	@Configuration
	static class Stack {
		@Bean
		public EmailClientBase emailClient() {
			return new MailinatorEmailClient();
		}
	}

}
