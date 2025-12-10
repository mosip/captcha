package io.mosip.captcha.dto;

import io.mosip.captcha.util.ErrorConstants;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class DTOTest {

	@Test
	public void testCaptchaRequestDTO() {
		CaptchaRequestDTO dto = new CaptchaRequestDTO();
		dto.setCaptchaToken("token123");
		dto.setModuleName("modA");

		assertEquals("token123", dto.getCaptchaToken());
		assertEquals("modA", dto.getModuleName());
	}

	@Test
	public void testCaptchaResponseDTO() {
		CaptchaResponseDTO dto = new CaptchaResponseDTO();
		dto.setSuccess(true);
		dto.setMessage("ok");

		assertTrue(dto.isSuccess());
		assertEquals("ok", dto.getMessage());
	}

	@Test
	public void testExceptionJSONInfoDTO() {
		ExceptionJSONInfoDTO dto = new ExceptionJSONInfoDTO("CODE1", "Error message");

		assertEquals("CODE1", dto.getErrorCode());
		assertEquals("Error message", dto.getMessage());

		// Test setters
		dto.setErrorCode("CODE2");
		dto.setMessage("Updated");

		assertEquals("CODE2", dto.getErrorCode());
		assertEquals("Updated", dto.getMessage());
	}

	@Test
	public void testGoogleReCaptchaV2Response() {
		GoogleReCaptchaV2Response dto = new GoogleReCaptchaV2Response();
		dto.setSuccess(true);
		dto.setChallengeTs("2024-01-01T12:00:00Z");
		dto.setHostname("test.host");
		dto.setErrorCodes(List.of("invalid-input"));

		assertTrue(dto.isSuccess());
		assertEquals("2024-01-01T12:00:00Z", dto.getChallengeTs());
		assertEquals("test.host", dto.getHostname());
		assertEquals(1, dto.getErrorCodes().size());
	}

	@Test
	public void testRequestWrapper() {
		RequestWrapper<String> wrapper = new RequestWrapper<>();

		wrapper.setId("ID001");
		wrapper.setVersion("1.0");

		Date now = new Date();
		wrapper.setRequesttime(now);

		wrapper.setRequest("myrequest");

		assertEquals("ID001", wrapper.getId());
		assertEquals("1.0", wrapper.getVersion());
		assertEquals("myrequest", wrapper.getRequest());

		// Defensive copy checks
		assertNotSame(now, wrapper.getRequesttime());
		assertEquals(now.getTime(), wrapper.getRequesttime().getTime());

		// Covers missing branch
		wrapper.setRequesttime(null);
		assertNull(wrapper.getRequesttime());
	}

	@Test
	public void testResponseWrapper() {
		ResponseWrapper<String> wrapper = new ResponseWrapper<>();
		wrapper.setId("ID100");
		wrapper.setVersion("2.0");
		wrapper.setResponsetime("2024-01-01T10:00:00Z");
		wrapper.setResponse("OK");

		ExceptionJSONInfoDTO err = new ExceptionJSONInfoDTO("ERR1", "msg");
		wrapper.setErrors(List.of(err));

		assertEquals("ID100", wrapper.getId());
		assertEquals("2.0", wrapper.getVersion());
		assertEquals("2024-01-01T10:00:00Z", wrapper.getResponsetime());
		assertEquals("OK", wrapper.getResponse());
		assertEquals(1, wrapper.getErrors().size());
	}

	@Test
	public void testEqualsHashCodeToString_forDTOs() {
		// CaptchaRequestDTO
		CaptchaRequestDTO r1 = new CaptchaRequestDTO();
		r1.setCaptchaToken("t1");
		r1.setModuleName("m1");

		CaptchaRequestDTO r2 = new CaptchaRequestDTO();
		r2.setCaptchaToken("t1");
		r2.setModuleName("m1");

		assertEquals(r1, r2);
		assertEquals(r1.hashCode(), r2.hashCode());
		assertNotNull(r1.toString());

		// CaptchaResponseDTO
		CaptchaResponseDTO c1 = new CaptchaResponseDTO();
		c1.setSuccess(true);
		c1.setMessage("ok");

		CaptchaResponseDTO c2 = new CaptchaResponseDTO();
		c2.setSuccess(true);
		c2.setMessage("ok");

		assertEquals(c1, c2);
		assertNotNull(c1.toString());

		// ExceptionJSONInfoDTO (does NOT implement equals)
		ExceptionJSONInfoDTO e1 = new ExceptionJSONInfoDTO("C1", "M1");
		ExceptionJSONInfoDTO e2 = new ExceptionJSONInfoDTO("C1", "M1");

		assertNotEquals(e1, e2);
		assertNotNull(e1.hashCode());
		assertNotNull(e1.toString());


		// GoogleReCaptchaV2Response
		GoogleReCaptchaV2Response g1 = new GoogleReCaptchaV2Response();
		g1.setSuccess(true);
		g1.setHostname("host");
		g1.setChallengeTs("ts");
		g1.setErrorCodes(List.of("x"));

		GoogleReCaptchaV2Response g2 = new GoogleReCaptchaV2Response();
		g2.setSuccess(true);
		g2.setHostname("host");
		g2.setChallengeTs("ts");
		g2.setErrorCodes(List.of("x"));

		assertEquals(g1, g2);
		assertNotNull(g1.toString());

		// RequestWrapper + ResponseWrapper
		RequestWrapper<String> w1 = new RequestWrapper<>();
		RequestWrapper<String> w2 = new RequestWrapper<>();
		w1.setId("ID1");
		w2.setId("ID1");
		w1.setVersion("v1");
		w2.setVersion("v1");
		w1.setRequest("R");
		w2.setRequest("R");

		assertNotEquals(w1, w2);
		assertNotNull(w1.toString());

		ResponseWrapper<String> rw1 = new ResponseWrapper<>();
		rw1.setId("ID");
		rw1.setVersion("v");
		rw1.setResponse("OK");

		assertNotNull(rw1.toString());
	}

	@Test
	public void testGoogleReCaptchaV2Response_nullErrors() {
		GoogleReCaptchaV2Response dto = new GoogleReCaptchaV2Response();
		dto.setErrorCodes(null);
		assertNull(dto.getErrorCodes());
	}

	@Test
	public void testResponseWrapper_nullErrors() {
		ResponseWrapper<String> wrapper = new ResponseWrapper<>();
		wrapper.setErrors(null);
		assertNull(wrapper.getErrors());
	}
}