package io.mosip.captcha.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class CommonConfigTest {

	private CommonConfig config;

	@Before
	public void setup() {
		config = new CommonConfig();
	}

	@Test
	public void testRestTemplateBean() {
		RestTemplate rt = config.restTemplate();
		assertNotNull(rt);

		// Call again to hit the method multiple times
		RestTemplate rt2 = config.restTemplate();
		assertNotNull(rt2);

		// They should be different instances (method returns new RestTemplate())
		assertNotSame(rt, rt2);
	}
}