package io.mosip.captcha.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class CommonConfigTest {

    @Test
    public void restTemplateBeanShouldBeCreated() {
        CommonConfig config = new CommonConfig();
        RestTemplate restTemplate = config.restTemplate();

        assertNotNull(restTemplate);
    }
}
