package io.mosip.captcha.controller;

import io.mosip.captcha.dto.*;
import io.mosip.captcha.exception.CaptchaException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.captcha.spi.CaptchaService;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class CaptchaController {

	@Autowired
	private CaptchaService captchaService;

	@PostMapping(path = "/validatecaptcha", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<CaptchaResponseDTO> validateCaptcha(@Valid @RequestBody RequestWrapper<CaptchaRequestDTO> captchaRequest) throws CaptchaException {
		log.debug("In captcha-validation-service controller to validate the recaptcha token", captchaRequest);
		return this.captchaService.validateCaptcha(captchaRequest.getRequest());
	}

}
