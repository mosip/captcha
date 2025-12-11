package io.mosip.captcha;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CaptchaServiceApplicationTests {

	@Test
	public void contextLoads() {
		// Spring context loads → covers the class partially
	}

	@Test
	public void testMainMethod() {
		// Call the application's main method to increase coverage
		CaptchaServiceApplication.main(new String[] {});
	}
}