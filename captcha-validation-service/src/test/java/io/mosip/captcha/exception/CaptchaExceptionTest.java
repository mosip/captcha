package io.mosip.captcha.exception;

import io.mosip.captcha.util.ErrorConstants;
import org.junit.Test;

import static org.junit.Assert.*;

public class CaptchaExceptionTest {

	@Test
	public void testConstructor_withOnlyErrorCode() {
		CaptchaException ex = new CaptchaException(ErrorConstants.INVALID_CAPTCHA_CODE);

		assertEquals(ErrorConstants.INVALID_CAPTCHA_CODE, ex.getErrorCode());
		assertEquals(ErrorConstants.getErrorMessage(ErrorConstants.INVALID_CAPTCHA_CODE),
				ex.getErrorMessage());
	}

	@Test
	public void testConstructor_withErrorCodeAndMessage() {
		CaptchaException ex = new CaptchaException("ERR123", "Custom error");

		assertEquals("ERR123", ex.getErrorCode());
		assertEquals("Custom error", ex.getErrorMessage());
	}
}