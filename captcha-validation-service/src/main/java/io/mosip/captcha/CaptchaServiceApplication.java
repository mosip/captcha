/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
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
