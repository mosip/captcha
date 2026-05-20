/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.provider;

import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.GoogleReCaptchaV2Response;
import io.mosip.captcha.exception.CaptchaException;
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
public class GoogleReCaptchaV2ProviderTest {

    @InjectMocks
    private GoogleReCaptchaV2Provider googleReCaptchaV2Provider;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(googleReCaptchaV2Provider, "secret", Map.ofEntries(Map.entry("preregistration", "preregsecret")));
        ReflectionTestUtils.setField(googleReCaptchaV2Provider, "verifyUrl", "https://www.google.com/recaptcha/api/siteverify");
    }

    @Test
    public void validateCaptcha_withValidInput_thenPass() throws CaptchaException {
		GoogleReCaptchaV2Response googleReCaptchaV2Response = new GoogleReCaptchaV2Response();
		googleReCaptchaV2Response.setHostname("https://www.google.com/recaptcha/api/siteverify");
		googleReCaptchaV2Response.setSuccess(true);
		googleReCaptchaV2Response.setChallengeTs("Success");

		Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(googleReCaptchaV2Response);
        CaptchaResponseDTO response = googleReCaptchaV2Provider.verifyCaptcha("preregistration", "captcha_token");
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void verifyCaptcha_withInvalidModuleName_thenException() {
        ReflectionTestUtils.setField(googleReCaptchaV2Provider, "secret", Collections.emptyMap());
        Assertions.assertThrows(CaptchaException.class, () -> {
            googleReCaptchaV2Provider.verifyCaptcha("invalid", "captcha_token");
        });
    }

    @Test
    public void verifyCaptcha_withRestClientFailure_thenException() {
        CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
        captchaRequest.setCaptchaToken("captcha_token");
        Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenThrow(new RestClientException("captcha token validation request failed"));
        Assertions.assertThrows(CaptchaException.class, () -> {
            googleReCaptchaV2Provider.verifyCaptcha("preregistration", "captcha_token");
        });
    }

    @Test
    public void verifyCaptcha_withInvalidToken_thenException() throws CaptchaException {
		GoogleReCaptchaV2Response captchaResponse = new GoogleReCaptchaV2Response();
		captchaResponse.setErrorCodes(List.of("Invalid token"));
		Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(captchaResponse);
        Assertions.assertThrows(CaptchaException.class, () -> {
            googleReCaptchaV2Provider.verifyCaptcha("preregistration", "invalid_captcha_token");
        });
    }
}
