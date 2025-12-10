package io.mosip.captcha.exception;

import io.mosip.captcha.dto.ExceptionJSONInfoDTO;
import io.mosip.captcha.dto.ResponseWrapper;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class InvalidRequestParameterExceptionTest {

	@Test
	public void testDefaultConstructor() {
		InvalidRequestParameterException ex = new InvalidRequestParameterException();
		assertNull(ex.getResponseWrapper());
		assertNull(ex.getExptionList());
		assertNull(ex.getOperation());
	}

	@Test
	public void testConstructor_withErrCodeMessageResponse() {
		ResponseWrapper<?> rw = new ResponseWrapper<>();
		InvalidRequestParameterException ex =
				new InvalidRequestParameterException("C1", "M1", rw);

		assertEquals(rw, ex.getResponseWrapper());
		assertNull(ex.getExptionList());
		assertNull(ex.getOperation());
	}

	@Test
	public void testConstructor_withRootCauseAndResponse() {
		ResponseWrapper<?> rw = new ResponseWrapper<>();
		Throwable cause = new RuntimeException("root");

		InvalidRequestParameterException ex =
				new InvalidRequestParameterException("C1", "M1", cause, rw);

		assertEquals(rw, ex.getResponseWrapper());
		assertNull(ex.getExptionList());
		assertNull(ex.getOperation());
	}

	@Test
	public void testConstructor_withExceptionListAndResponse() {
		ResponseWrapper<?> rw = new ResponseWrapper<>();
		List<ExceptionJSONInfoDTO> list =
				List.of(new ExceptionJSONInfoDTO("E1", "M1"));

		InvalidRequestParameterException ex =
				new InvalidRequestParameterException(list, rw);

		assertEquals(rw, ex.getResponseWrapper());
		assertEquals(list, ex.getExptionList());
		assertNull(ex.getOperation());
	}

	@Test
	public void testConstructor_withExceptionListOperationAndResponse() {
		ResponseWrapper<?> rw = new ResponseWrapper<>();
		List<ExceptionJSONInfoDTO> list =
				List.of(new ExceptionJSONInfoDTO("E1", "M1"));

		InvalidRequestParameterException ex =
				new InvalidRequestParameterException(list, "OP123", rw);

		assertEquals(rw, ex.getResponseWrapper());
		assertEquals(list, ex.getExptionList());
		assertEquals("OP123", ex.getOperation());
	}
}