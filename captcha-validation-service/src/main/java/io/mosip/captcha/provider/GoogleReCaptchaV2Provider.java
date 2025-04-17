/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.provider;

import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.GoogleReCaptchaV2Response;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.spi.CaptchaProvider;
import io.mosip.captcha.util.ErrorConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "mosip.captcha.googlerecaptchav2")
@Slf4j
public class GoogleReCaptchaV2Provider implements CaptchaProvider {

    @Getter
    @Setter
    private Map<String, String> secret;

    @Getter
    @Setter
    private String verifyUrl;

    @Autowired
    private RestTemplate restTemplate;

    private final String CAPTCHA_SUCCESS = "Captcha successfully verified";

    public String getProviderName() {
        return "GoogleReCaptchaV2";
    }

    @Override
    public CaptchaResponseDTO verifyCaptcha(String moduleName, String captchaToken) throws CaptchaException {

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        String captchaSecret = secret.get(moduleName);
        if(captchaSecret == null) {
            log.error("Failed to find secret for module {}", moduleName);
            throw new CaptchaException(ErrorConstants.CAPTCHA_VALIDATION_FAILED);
        }

        param.add("secret", captchaSecret);
        param.add("response", captchaToken);

        log.info("validate the token request via {}", verifyUrl);

        GoogleReCaptchaV2Response captchaResponse = null;
        try {
            captchaResponse = this.restTemplate.postForObject(verifyUrl, param, GoogleReCaptchaV2Response.class);
            log.info(" captchaResponse -> {}", captchaResponse);
        } catch (RestClientException ex) {
            log.error("captcha token validation request failed", ex);
        }

        if(captchaResponse == null)
            throw new CaptchaException(ErrorConstants.CAPTCHA_VALIDATION_FAILED);

        if(!CollectionUtils.isEmpty(captchaResponse.getErrorCodes()))
            throw new CaptchaException(captchaResponse.getErrorCodes().get(0), captchaResponse.getErrorCodes().get(0));

        if(captchaResponse.isSuccess()) {
            CaptchaResponseDTO response = new CaptchaResponseDTO();
            response.setMessage(CAPTCHA_SUCCESS);
            response.setSuccess(captchaResponse.isSuccess());
            return response;
        }

        //request is NOT success and error-codes is empty
        throw new CaptchaException(ErrorConstants.CAPTCHA_VALIDATION_FAILED);
    }
}
