package io.mosip.captcha.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class CaptchaUtilsTest {

    @Test
    public void testGetCurrentResponseTimeNotNull() {
        String responseTime = CaptchaUtils.getCurrentResponseTime();
        assertNotNull("Response time should not be null", responseTime);
    }

    @Test
    public void testGetCurrentResponseTimeFormat() {
        String responseTime = CaptchaUtils.getCurrentResponseTime();
        // Basic check: ISO format ends with 'Z' for UTC
        assertTrue("Response time should end with Z", responseTime.endsWith("Z"));
        // Optional: check length roughly matches ISO string length
        assertTrue("Response time length should be > 10", responseTime.length() > 10);
    }
}
