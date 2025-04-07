package io.mosip.captcha.service;

import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.spi.CaptchaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.spi.CaptchaService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {

	@Value("${mosip.captcha.api.id}")
	private String captchaApiId;

	@Value("${mosip.captcha.api.version}")
	private String captchaApiVersion;

	@Autowired
	private CaptchaProvider captchaProvider;

	@Override
	public ResponseWrapper<CaptchaResponseDTO> validateCaptcha(CaptchaRequestDTO captchaRequest) throws CaptchaException {
		ResponseWrapper<CaptchaResponseDTO> responseWrapper = captchaProvider.verifyCaptcha(captchaRequest.getModuleName(), captchaRequest.getCaptchaToken().trim());
		responseWrapper.setId(captchaApiId);
		responseWrapper.setVersion(captchaApiVersion);
		return responseWrapper;
	}

}
