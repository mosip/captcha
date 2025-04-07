package io.mosip.captcha.config;

import io.mosip.captcha.provider.GoogleCaptchaProvider;
import io.mosip.captcha.spi.CaptchaProvider;
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

	@Bean
	public CaptchaProvider captchaProvider() {
//        if("abc-captcha-provider".equals(captchaProviderName)) {
//            return new ABCCaptchaProvider();
//        }
		return new GoogleCaptchaProvider();   // default captcha provider
	}
}
