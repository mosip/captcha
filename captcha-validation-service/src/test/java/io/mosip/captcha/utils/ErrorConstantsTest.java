package io.mosip.captcha.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ErrorConstantsTest {

	@Test
	public void testErrorMessageForValidCodes() {
		assertEquals("Invalid Captcha entered",
				ErrorConstants.getErrorMessage(ErrorConstants.INVALID_CAPTCHA_CODE));

		assertEquals("Invalid request , Captcha token can't be null or empty",
				ErrorConstants.getErrorMessage(ErrorConstants.INVALID_CAPTCHA_REQUEST));

		assertEquals("Captcha validation failed",
				ErrorConstants.getErrorMessage(ErrorConstants.CAPTCHA_VALIDATION_FAILED));
	}

	@Test
	public void testErrorMessageForUnknownCode() {
		assertNull(ErrorConstants.getErrorMessage("UNKNOWN_CODE"));
	}

	@Test
	public void testConstantsNotNull() {
		assertNotNull(ErrorConstants.INVALID_CAPTCHA_CODE);
		assertNotNull(ErrorConstants.INVALID_CAPTCHA_REQUEST);
		assertNotNull(ErrorConstants.CAPTCHA_VALIDATION_FAILED);
	}
}