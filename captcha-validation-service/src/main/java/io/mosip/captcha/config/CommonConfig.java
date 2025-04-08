package io.mosip.captcha.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommonConfig {

	@Value("${mosip.captcha.captcha-provider-name:#{null}}")
	private String captchaProviderName;

	@Primary
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
