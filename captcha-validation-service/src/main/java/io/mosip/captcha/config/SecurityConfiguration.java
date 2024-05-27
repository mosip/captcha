package io.mosip.captcha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Configuration
public class SecurityConfiguration {

    @Bean
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(httpEntry -> httpEntry.disable())
                .authorizeRequests().requestMatchers("/").permitAll();
    }
}
