package io.mosip.captcha.advice;

import io.mosip.captcha.dto.ExceptionJSONInfoDTO;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.util.CaptchaUtils;
import io.mosip.captcha.util.ErrorConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class CaptchaExceptionHandlerTest {

	private CaptchaExceptionHandler handler;

	@Before
	public void setup() {
		handler = new CaptchaExceptionHandler();

		// Inject values via reflection
		setField(handler, "captchaApiId", "captcha-api");
		setField(handler, "captchaApiVersion", "v1");

		// Mock environment (even though unused)
		Environment mockEnv = Mockito.mock(Environment.class);
		setField(handler, "env", mockEnv);
	}

	/** Utility to inject private fields */
	private void setField(Object target, String name, Object value) {
		try {
			Field f = target.getClass().getDeclaredField(name);
			f.setAccessible(true);
			f.set(target, value);
		} catch (Exception ignored) {}
	}

	/** Covers: record creation, list creation, error population, ResponseEntity branch */
	@Test
	public void testHandleInvalidCaptchaRequest_full() {
		BeanPropertyBindingResult result =
				new BeanPropertyBindingResult(new Object(), "object");

		MethodArgumentNotValidException ex =
				new MethodArgumentNotValidException(null, result);

		ResponseEntity<ResponseWrapper<?>> response = handler.handleInvalidCaptchaRequest(ex);

		assertNotNull(response);
		assertEquals("captcha-api", response.getBody().getId());
		assertEquals("v1", response.getBody().getVersion());
		assertNull(response.getBody().getResponse());
		assertNotNull(response.getBody().getErrors());
		assertEquals(1, response.getBody().getErrors().size());

		ExceptionJSONInfoDTO err = response.getBody().getErrors().get(0);
		assertEquals(ErrorConstants.INVALID_CAPTCHA_REQUEST, err.getErrorCode());
		assertEquals(ErrorConstants.getErrorMessage(ErrorConstants.INVALID_CAPTCHA_REQUEST), err.getMessage());
	}

	/** Covers: normal CaptchaException path, list creation, returning wrapper directly */
	@Test
	public void testHandleCaptchaException_full() {
		CaptchaException ex = new CaptchaException("ERR100", "Sample error message");

		ResponseWrapper<?> response = handler.handleCaptchaException(ex);

		assertNotNull(response);
		assertEquals("captcha-api", response.getId());
		assertEquals("v1", response.getVersion());
		assertEquals(1, response.getErrors().size());

		ExceptionJSONInfoDTO err = response.getErrors().get(0);
		assertEquals("ERR100", err.getErrorCode());
		assertEquals("Sample error message", err.getMessage());
	}

	/** Covers: general exception path, message extraction, null & list edge */
	@Test
	public void testHandleGeneralException_full() {
		Exception ex = new Exception("GeneralFailure");

		ResponseWrapper<?> response = handler.handleException(ex);

		assertNotNull(response);
		assertEquals("captcha-api", response.getId());
		assertEquals("v1", response.getVersion());
		assertNotNull(response.getErrors());
		assertEquals(1, response.getErrors().size());

		ExceptionJSONInfoDTO err = response.getErrors().get(0);
		assertEquals(ErrorConstants.CAPTCHA_VALIDATION_FAILED, err.getErrorCode());
		assertEquals("GeneralFailure", err.getMessage());
	}

	/** Extra coverage: null Exception message (forces null message branch) */
	@Test
	public void testHandleGeneralException_nullMessage() {
		Exception ex = new Exception((String) null);

		ResponseWrapper<?> response = handler.handleException(ex);

		assertNotNull(response);
		assertEquals(ErrorConstants.CAPTCHA_VALIDATION_FAILED,
				response.getErrors().get(0).getErrorCode());
		assertNull(response.getErrors().get(0).getMessage());
	}

	/** Extra coverage: multiple errors + setter/getter Jacoco hit for DTO */
	@Test
	public void testExceptionJSONInfoDTO_gettersSettersCoverage() {
		ExceptionJSONInfoDTO dto = new ExceptionJSONInfoDTO();
		dto.setErrorCode("X001");
		dto.setMessage("TestMessage");

		assertEquals("X001", dto.getErrorCode());
		assertEquals("TestMessage", dto.getMessage());

		// toString() coverage
		assertTrue(dto.toString().contains("X001"));
	}
}