package io.mosip.captcha.controller;

import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.exception.InvalidRequestParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.MainRequestDTO;
import io.mosip.captcha.exception.InvalidRequestCaptchaException;
import io.mosip.captcha.spi.CaptchaService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CaptchaController {

	private static final String VALIDATE = "validate";


	@Autowired
	private CaptchaService captchaService;

	@PostMapping(path = "/validatecaptcha", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateCaptcha(@Validated @RequestBody MainRequestDTO<CaptchaRequestDTO> captchaRequest,
			Errors errors) throws InvalidRequestParameterException, CaptchaException, InvalidRequestCaptchaException {
		log.info("In captcha-validation-service controller to validate the recaptcha token" + captchaRequest);
		return new ResponseEntity<>(this.captchaService.validateCaptcha(captchaRequest.getRequest()), HttpStatus.OK);
	}

}
