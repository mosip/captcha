package io.mosip.captcha.serviceimpl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
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

	@Value("${mosip.captcha.recaptcha.verify.url}")
	public String recaptchaVerifyUrl;

	@Value("${mosip.captcha.id.validate}")
	public String mosipcaptchaValidateId;

	@Value("${mosip.captcha.validate.api.version}")
	private String version;

	@Mock
	private RestTemplate restTemplate;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Map<String, String> secrets = new HashMap<>();
		secrets.put("resident", "resident-captcha-secret");
		secrets.put("preregistration", "pre-registration-captcha-secret");
		ReflectionTestUtils.setField(captchaServiceImpl, "secret", secrets);
		ReflectionTestUtils.setField(captchaServiceImpl, "captchaVerifyUrl",
				"https://www.google.com/recaptcha/api/siteverify");
		ReflectionTestUtils.setField(captchaServiceImpl, "captchaApiId", "123");
		ReflectionTestUtils.setField(captchaServiceImpl, "captchaApiVersion", "2.0");
	}

	
	@Test
	public void validateCaptcha_withValidInput_thenPass() throws CaptchaException, InvalidRequestCaptchaException {
		CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
		captchaRequest.setCaptchaToken("temp");

		GoogleReCaptchaV2Response googleReCaptchaV2Response = new GoogleReCaptchaV2Response();
		googleReCaptchaV2Response.setHostname(recaptchaVerifyUrl);
		googleReCaptchaV2Response.setSuccess(true);
		googleReCaptchaV2Response.setChallengeTs("Success");

		CaptchaResponseDTO res = new CaptchaResponseDTO();
		res.setMessage("captcha scuccessfully set");
		res.setSuccess(true);
		ResponseWrapper<CaptchaResponseDTO> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(res);
		responseWrapper.setId(mosipcaptchaValidateId);
		responseWrapper.setVersion(version);

		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("secret", "pre-registration-captcha-secret");
		param.add("response", "temp");

		Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(googleReCaptchaV2Response);
		assertNotNull(captchaServiceImpl.validateCaptcha(captchaRequest));
	}

	@Test(expected = InvalidRequestCaptchaException.class)
	public void validateCaptcha_withInvalidRequest_throwException() throws CaptchaException, InvalidRequestCaptchaException {
		CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
		GoogleReCaptchaV2Response captchaResponse = new GoogleReCaptchaV2Response();
		captchaResponse.setHostname(recaptchaVerifyUrl);
		captchaResponse.setSuccess(true);
		captchaResponse.setChallengeTs("Success");
		captchaServiceImpl.validateCaptcha(captchaRequest);
	}

}
