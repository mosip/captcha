package io.mosip.captcha.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.mosip.captcha.util.CaptchaErrorCode;
import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.util.CaptchaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.ExceptionJSONInfoDTO;
import io.mosip.captcha.dto.GoogleCaptchaDTO;
import io.mosip.captcha.dto.MainResponseDTO;
import io.mosip.captcha.exception.InvalidRequestCaptchaException;
import io.mosip.captcha.spi.CaptchaService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {

	@Value("#{${mosip.captcha.secret-key}}")
	public Map<String,String> secretKeys;

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

		log.info("In captcha service to validate the token request"
				+ ((CaptchaRequestDTO) captchaRequest).getCaptchaToken());

		validateCaptchaRequest((CaptchaRequestDTO) captchaRequest);
		String moduleName = ((CaptchaRequestDTO) captchaRequest).getModuleName();
		MainResponseDTO<CaptchaResponseDTO> mainResponse = new MainResponseDTO<>();

		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("secret", secretKeys.get(moduleName == null? defaultModuleName : moduleName));
		param.add("response", ((CaptchaRequestDTO) captchaRequest).getCaptchaToken().trim());

		GoogleCaptchaDTO captchaResponse = null;

		try {
			log.info("In captcha service try block to validate the token request via a google verify site rest call"
							+ ((CaptchaRequestDTO) captchaRequest).getCaptchaToken() + captchaVerifyUrl);

			captchaResponse = this.restTemplate.postForObject(captchaVerifyUrl, param, GoogleCaptchaDTO.class);
			log.debug(" captchaResponse -> {}", captchaResponse);

		} catch (RestClientException ex) {
			log.error("In captcha service to validate the token request via a google verify site rest call has failed --->"
							+ ((CaptchaRequestDTO) captchaRequest).getCaptchaToken() + captchaVerifyUrl
							, ex);
			if (captchaResponse != null && captchaResponse.getErrorCodes() !=null) {
				throw new CaptchaException(captchaResponse.getErrorCodes().get(0), captchaResponse.getErrorCodes().get(0));
			}
		}

		if (captchaResponse!=null && captchaResponse.isSuccess()) {
			log.info("In captcha service token request has been successfully verified --->"
							+ captchaResponse.isSuccess());
			mainResponse.setId(captchaApiId);
			mainResponse.setResponsetime(captchaResponse.getChallengeTs());
			mainResponse.setVersion(captchaApiVersion);
			CaptchaResponseDTO response = new CaptchaResponseDTO();
			response.setMessage(CAPTCHA_SUCCESS);
			response.setSuccess(captchaResponse.isSuccess());
			mainResponse.setResponse(response);
		} else {
			if (captchaResponse != null) {
				log.error("In captcha service token request has failed --->"
								+ captchaResponse.isSuccess());
			}
			mainResponse.setId(captchaApiId);
			mainResponse.setResponsetime(CaptchaUtils.getCurrentResponseTime());
			mainResponse.setVersion(captchaApiVersion);
			mainResponse.setResponse(null);
			ExceptionJSONInfoDTO error = new ExceptionJSONInfoDTO(CaptchaErrorCode.INVALID_CAPTCHA_CODE.getErrorCode(),
					CaptchaErrorCode.INVALID_CAPTCHA_CODE.getErrorMessage());
			List<ExceptionJSONInfoDTO> errorList = new ArrayList<ExceptionJSONInfoDTO>();
			errorList.add(error);
			mainResponse.setErrors(errorList);

		}
		return mainResponse;
	}

	private void validateCaptchaRequest(CaptchaRequestDTO captchaRequest) throws InvalidRequestCaptchaException {

	 if (captchaRequest.getCaptchaToken() == null || captchaRequest.getCaptchaToken().trim().length() == 0) {
		 	log.debug("{}", captchaRequest);
			throw new InvalidRequestCaptchaException(CaptchaErrorCode.INVALID_CAPTCHA_REQUEST.getErrorCode(),
					CaptchaErrorCode.INVALID_CAPTCHA_REQUEST.getErrorMessage());
		}
	}

}
