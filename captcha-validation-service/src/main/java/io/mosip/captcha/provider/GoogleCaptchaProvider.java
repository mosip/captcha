package io.mosip.captcha.provider;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.spi.CaptchaProvider;
import io.mosip.captcha.util.ErrorConstants;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "mosip.captcha")
@Slf4j
public class GoogleCaptchaProvider implements CaptchaProvider {

    @Getter
    @Setter
    private Map<String, String> secret;

    @Value("${mosip.captcha.verify-url}")
    private String captchaVerifyUrl;

    @Value("${mosip.captcha.default.module-name:preregistration}")
    private String defaultModuleName;

    @Autowired
    private RestTemplate restTemplate;

    private final String CAPTCHA_SUCCESS = "Captcha successfully verified";

    @Data
    public static class GoogleReCaptchaV2Response implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        @JsonProperty("success")
        private boolean success;

        @JsonProperty("challenge_ts")
        private String challengeTs;

        @JsonProperty("hostname")
        private String hostname;

        @JsonProperty("error-codes")
        private List<String> errorCodes;

    }


    @Override
    public ResponseWrapper<CaptchaResponseDTO> verifyCaptcha(String moduleName, String captchaToken) throws CaptchaException {

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        String captchaSecret = secret.get(moduleName == null ? defaultModuleName : moduleName);
        if(captchaSecret == null) {
            log.error("Failed to find secret for module {}", moduleName);
            throw new CaptchaException(ErrorConstants.CAPTCHA_VALIDATION_FAILED);
        }

        param.add("secret", captchaSecret);
        param.add("response", captchaToken);

        log.info("validate the token request via {}", captchaVerifyUrl);

        GoogleReCaptchaV2Response captchaResponse = null;
        try {
            captchaResponse = this.restTemplate.postForObject(captchaVerifyUrl, param, GoogleReCaptchaV2Response.class);
            log.info(" captchaResponse -> {}", captchaResponse);
        } catch (RestClientException ex) {
            log.error("captcha token validation request failed", ex);
        }

        if(captchaResponse == null)
            throw new CaptchaException(ErrorConstants.CAPTCHA_VALIDATION_FAILED);

        if(!CollectionUtils.isEmpty(captchaResponse.getErrorCodes()))
            throw new CaptchaException(captchaResponse.getErrorCodes().get(0), captchaResponse.getErrorCodes().get(0));

        if(captchaResponse.isSuccess()) {
            ResponseWrapper<CaptchaResponseDTO> responseWrapper = new ResponseWrapper<>();
            responseWrapper.setResponsetime(captchaResponse.getChallengeTs());
            CaptchaResponseDTO response = new CaptchaResponseDTO();
            response.setMessage(CAPTCHA_SUCCESS);
            response.setSuccess(captchaResponse.isSuccess());
            responseWrapper.setResponse(response);
            return responseWrapper;
        }

        //request is NOT success and error-codes is empty
        throw new CaptchaException(ErrorConstants.CAPTCHA_VALIDATION_FAILED);
    }
}
