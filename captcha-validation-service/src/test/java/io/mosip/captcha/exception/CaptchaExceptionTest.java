package io.mosip.captcha.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.mosip.captcha.util.ErrorConstants;

public class CaptchaExceptionTest {

    @Test
    public void testConstructorWithErrorCode() {
        String errorCode = "CAPTCHA_001";
        CaptchaException ex = new CaptchaException(errorCode);

        // check errorCode is set
        assertEquals(errorCode, ex.getErrorCode());

        // check errorMessage comes from ErrorConstants
        assertEquals(ErrorConstants.getErrorMessage(errorCode), ex.getErrorMessage());
    }

    @Test
    public void testConstructorWithErrorCodeAndMessage() {
        String errorCode = "CAPTCHA_002";
        String customMessage = "Custom captcha error message";
        CaptchaException ex = new CaptchaException(errorCode, customMessage);

        assertEquals(errorCode, ex.getErrorCode());
        assertEquals(customMessage, ex.getErrorMessage());
    }

    @Test
    public void testSettersAndGetters() {
        CaptchaException ex = new CaptchaException("dummy", "dummy message");

        ex.setErrorCode("NEW_CODE");
        ex.setErrorMessage("New message");

        assertEquals("NEW_CODE", ex.getErrorCode());
        assertEquals("New message", ex.getErrorMessage());
    }
}
