package io.mosip.captcha.service;

import java.util.Map;

import io.mosip.captcha.util.CaptchaErrorCode;
import io.mosip.captcha.exception.CaptchaException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.GoogleReCaptchaV2Response;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.exception.InvalidRequestCaptchaException;
import io.mosip.captcha.spi.CaptchaService;
import lombok.extern.slf4j.Slf4j;

@ConfigurationProperties(prefix = "mosip.captcha")
@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {

	@Getter
	@Setter
	private Map<String, String> secret;

	@Value("${mosip.captcha.verify-url}")
	public String captchaVerifyUrl;

	@Value("${mosip.captcha.default.module-name:preregistration}")
	public String defaultModuleName;

	@Value("${mosip.captcha.api.id}")
	public String captchaApiId;

	@Value("${mosip.captcha.api.version}")
	private String captchaApiVersion;

	@Autowired
	private RestTemplate restTemplate;

	private final String CAPTCHA_SUCCESS = " Captcha successfully verified";

	@Override
	public Object validateCaptcha(Object captchaRequest) throws CaptchaException, InvalidRequestCaptchaException {
		validateCaptchaRequest((CaptchaRequestDTO) captchaRequest);
		String moduleName = ((CaptchaRequestDTO) captchaRequest).getModuleName();

		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("secret", secret.get(moduleName == null? defaultModuleName : moduleName));
		param.add("response", ((CaptchaRequestDTO) captchaRequest).getCaptchaToken().trim());

		if(param.get("secret") == null) {
			log.error("Failed to find secret for module {}", moduleName);
			throw new CaptchaException(CaptchaErrorCode.CAPTCHA_VALIDATION_FAILED.getErrorCode(),
					CaptchaErrorCode.CAPTCHA_VALIDATION_FAILED.getErrorCode());
		}

		GoogleReCaptchaV2Response captchaResponse = null;
		try {
			log.info("validate the token request via {}", captchaVerifyUrl);
			captchaResponse = this.restTemplate.postForObject(captchaVerifyUrl, param, GoogleReCaptchaV2Response.class);
			log.debug(" captchaResponse -> {}", captchaResponse);
		} catch (RestClientException ex) {
			log.error("captcha token validation request failed", ex);
		}

		if(captchaResponse == null)
			throw new CaptchaException(CaptchaErrorCode.CAPTCHA_VALIDATION_FAILED.getErrorCode(),
					CaptchaErrorCode.CAPTCHA_VALIDATION_FAILED.getErrorCode());

		if(!CollectionUtils.isEmpty(captchaResponse.getErrorCodes()))
			throw new CaptchaException(captchaResponse.getErrorCodes().get(0), captchaResponse.getErrorCodes().get(0));

		if(captchaResponse.isSuccess()) {
			ResponseWrapper<CaptchaResponseDTO> responseWrapper = new ResponseWrapper<>();
			responseWrapper.setId(captchaApiId);
			responseWrapper.setResponsetime(captchaResponse.getChallengeTs());
			responseWrapper.setVersion(captchaApiVersion);
			CaptchaResponseDTO response = new CaptchaResponseDTO();
			response.setMessage(CAPTCHA_SUCCESS);
			response.setSuccess(captchaResponse.isSuccess());
			responseWrapper.setResponse(response);
			return responseWrapper;
		}

		//request is NOT success and error-codes is empty
		throw new CaptchaException(CaptchaErrorCode.CAPTCHA_VALIDATION_FAILED.getErrorCode(),
				CaptchaErrorCode.CAPTCHA_VALIDATION_FAILED.getErrorCode());
	}

	private void validateCaptchaRequest(CaptchaRequestDTO captchaRequest) throws InvalidRequestCaptchaException {
		log.debug("{}", captchaRequest);
		if (captchaRequest.getCaptchaToken() == null || captchaRequest.getCaptchaToken().trim().length() == 0) {
			throw new InvalidRequestCaptchaException(CaptchaErrorCode.INVALID_CAPTCHA_REQUEST.getErrorCode(),
					CaptchaErrorCode.INVALID_CAPTCHA_REQUEST.getErrorMessage());
		}
	}

}
