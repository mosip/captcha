package io.mosip.captcha.service;

import static org.junit.Assert.*;

import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.GoogleReCaptchaV2Response;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.exception.CaptchaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class CaptchaServiceImplTest {

	@InjectMocks
	private CaptchaServiceImpl captchaServiceImpl;

	@Value("${mosip.captcha.recaptcha.verify.url}")
	public String recaptchaVerifyUrl;

	@Mock
	private RestTemplate restTemplate;

	@Before
	public void setUp() {
		Map<String, String> secrets = new HashMap<>();
		secrets.put("resident", "resident-captcha-secret");
		secrets.put("preregistration", "pre-registration-captcha-secret");
		ReflectionTestUtils.setField(captchaServiceImpl, "secret", secrets);
		ReflectionTestUtils.setField(captchaServiceImpl, "captchaVerifyUrl",
				"https://www.google.com/recaptcha/api/siteverify");
		ReflectionTestUtils.setField(captchaServiceImpl, "defaultModuleName", "preregistration");
		ReflectionTestUtils.setField(captchaServiceImpl, "captchaApiId", "123");
		ReflectionTestUtils.setField(captchaServiceImpl, "captchaApiVersion", "2.0");
	}

	
	@Test
	public void validateCaptcha_withValidInput_thenPass() throws CaptchaException {
		CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
		captchaRequest.setCaptchaToken("temp");

		GoogleReCaptchaV2Response googleReCaptchaV2Response = new GoogleReCaptchaV2Response();
		googleReCaptchaV2Response.setHostname(recaptchaVerifyUrl);
		googleReCaptchaV2Response.setSuccess(true);
		googleReCaptchaV2Response.setChallengeTs("Success");

		Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(googleReCaptchaV2Response);
		ResponseWrapper<CaptchaResponseDTO> responseWrapper = captchaServiceImpl.validateCaptcha(captchaRequest);
		assertNotNull(responseWrapper);
		assertNotNull(responseWrapper.getResponse());
		assertTrue(responseWrapper.getResponse().isSuccess());
	}

	@Test(expected = CaptchaException.class)
	public void validateCaptcha_withInvalidModuleName_throwException() throws CaptchaException {
		ReflectionTestUtils.setField(captchaServiceImpl, "secret", Collections.emptyMap());

		CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
		captchaRequest.setCaptchaToken("captcha_token");
		captchaRequest.setModuleName("invalid");
		captchaServiceImpl.validateCaptcha(captchaRequest);
	}

	@Test(expected = CaptchaException.class)
	public void validateCaptcha_withRestClientFailure_throwException() throws CaptchaException {
		CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
		captchaRequest.setCaptchaToken("captcha_token");
		Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenThrow(new RestClientException("captcha token validation request failed"));
		captchaServiceImpl.validateCaptcha(captchaRequest);
	}

	@Test(expected = CaptchaException.class)
	public void validateCaptcha_withInvalidToken_throwException() throws CaptchaException {
		CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
		captchaRequest.setCaptchaToken("captcha_token");

		GoogleReCaptchaV2Response captchaResponse = new GoogleReCaptchaV2Response();
		captchaResponse.setErrorCodes(List.of("Invalid token"));

		Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(captchaResponse);
		captchaServiceImpl.validateCaptcha(captchaRequest);
	}

}
