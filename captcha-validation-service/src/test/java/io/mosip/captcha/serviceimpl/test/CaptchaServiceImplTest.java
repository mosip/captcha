package io.mosip.captcha.serviceimpl.test;

import static org.junit.Assert.*;

import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.GoogleReCaptchaV2Response;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.exception.InvalidRequestCaptchaException;
import io.mosip.captcha.service.CaptchaServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
@SpringBootTest
@ContextConfiguration(classes = { CaptchaServiceImpl.class })
public class CaptchaServiceImplTest {

	@InjectMocks
	private CaptchaServiceImpl captchaServiceImpl;

	@Value("${mosip.captcha.secretkey}")
	public String recaptchaSecret;

	@Value("${mosip.captcha.recaptcha.verify.url}")
	public String recaptchaVerifyUrl;

	@Value("${mosip.captcha.id.validate}")
	public String mosipcaptchaValidateId;

	@Value("${mosip.captcha.validate.api.version}")
	private String version;

	@Mock
	private RestTemplate restTemplate;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Map<String, String> secrets = new HashMap<>();
		secrets.put("resident", "resident-captcha-secret");
		secrets.put("preregistration", "pre-registration-captcha-secret");
		ReflectionTestUtils.setField(captchaServiceImpl, "secrets", secrets);
		ReflectionTestUtils.setField(captchaServiceImpl, "captchaVerifyUrl",
				"https://www.google.com/recaptcha/api/siteverify");
		ReflectionTestUtils.setField(captchaServiceImpl, "captchaApiId", "123");
		ReflectionTestUtils.setField(captchaServiceImpl, "captchaApiVersion", "2.0");
	}

	
	@Test
	public void validateCaptchaTest() throws CaptchaException, InvalidRequestCaptchaException {
		CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
		
		ResponseWrapper<CaptchaResponseDTO> mainResponse = new ResponseWrapper<>();
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("secret", recaptchaSecret);
		GoogleReCaptchaV2Response captchaResponse = new GoogleReCaptchaV2Response();
		captchaResponse.setHostname(recaptchaVerifyUrl);
		captchaResponse.setSuccess(true);
		captchaResponse.setChallengeTs("Success");

		captchaRequest.setCaptchaToken("temp");
		captchaRequest.getCaptchaToken();

		CaptchaResponseDTO res = new CaptchaResponseDTO();
		res.setMessage("captcha scuccessfully set");
		res.setSuccess(true);
		mainResponse.setResponse(res);
		mainResponse.setId(mosipcaptchaValidateId);
		mainResponse.setVersion(version);

		Mockito.when(restTemplate.postForObject("https://www.google.com/recaptcha/api/siteverify",
				"{secret=[demo], response=[aRsasahksasa]}", GoogleReCaptchaV2Response.class)).thenReturn(captchaResponse);
		assertNotNull(captchaServiceImpl.validateCaptcha(captchaRequest));
	}

	@Test(expected = InvalidRequestCaptchaException.class)
	public void validateCaptchaExceptionTest() throws CaptchaException, InvalidRequestCaptchaException {
		CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
		GoogleReCaptchaV2Response captchaResponse = new GoogleReCaptchaV2Response();
		captchaResponse.setHostname(recaptchaVerifyUrl);
		captchaResponse.setSuccess(true);
		captchaResponse.setChallengeTs("Success");
		captchaServiceImpl.validateCaptcha(captchaRequest);
	}

}
