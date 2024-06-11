package io.mosip.captcha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthContributorAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceHealthContributorAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = { "io.mosip.captcha.*", })
public class CaptchaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaptchaServiceApplication.class, args);
	}

}
