package io.mosip.captcha.provider;

import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.provider.GoogleCaptchaProvider.GoogleReCaptchaV2Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class GoogleCaptchaProviderTest {

    @InjectMocks
    private GoogleCaptchaProvider googleCaptchaProvider;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(googleCaptchaProvider, "secret", Map.ofEntries(Map.entry("preregistration", "preregsecret")));
        ReflectionTestUtils.setField(googleCaptchaProvider, "captchaVerifyUrl", "https://www.google.com/recaptcha/api/siteverify");
        ReflectionTestUtils.setField(googleCaptchaProvider, "defaultModuleName", "preregistration");
    }

    @Test
    public void validateCaptcha_withValidInput_thenPass() throws CaptchaException {
		GoogleReCaptchaV2Response googleReCaptchaV2Response = new GoogleReCaptchaV2Response();
		googleReCaptchaV2Response.setHostname("https://www.google.com/recaptcha/api/siteverify");
		googleReCaptchaV2Response.setSuccess(true);
		googleReCaptchaV2Response.setChallengeTs("Success");

		Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(googleReCaptchaV2Response);
        ResponseWrapper<CaptchaResponseDTO> responseWrapper = googleCaptchaProvider.verifyCaptcha("preregistration", "captcha_token");
        Assertions.assertNotNull(responseWrapper);
        Assertions.assertNotNull(responseWrapper.getResponse());
        Assertions.assertTrue(responseWrapper.getResponse().isSuccess());
    }

    @Test
    public void verifyCaptcha_withInvalidModuleName_thenException() {
        ReflectionTestUtils.setField(googleCaptchaProvider, "secret", Collections.emptyMap());
        Assertions.assertThrows(CaptchaException.class, () -> {
            googleCaptchaProvider.verifyCaptcha("invalid", "captcha_token");
        });
    }

    @Test
    public void verifyCaptcha_withRestClientFailure_thenException() {
        CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
        captchaRequest.setCaptchaToken("captcha_token");
        Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenThrow(new RestClientException("captcha token validation request failed"));
        Assertions.assertThrows(CaptchaException.class, () -> {
            googleCaptchaProvider.verifyCaptcha("preregistration", "captcha_token");
        });
    }

    @Test
    public void verifyCaptcha_withInvalidToken_thenException() throws CaptchaException {
		GoogleReCaptchaV2Response captchaResponse = new GoogleReCaptchaV2Response();
		captchaResponse.setErrorCodes(List.of("Invalid token"));
		Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(captchaResponse);
        Assertions.assertThrows(CaptchaException.class, () -> {
            googleCaptchaProvider.verifyCaptcha("preregistration", "invalid_captcha_token");
        });
    }
}
