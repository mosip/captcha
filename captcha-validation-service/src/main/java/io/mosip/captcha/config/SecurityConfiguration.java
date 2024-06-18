package io.mosip.captcha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(cfg -> cfg.disable());
        httpSecurity.authorizeHttpRequests(
                http -> http.anyRequest().permitAll());
        return httpSecurity.build();
    }
}
