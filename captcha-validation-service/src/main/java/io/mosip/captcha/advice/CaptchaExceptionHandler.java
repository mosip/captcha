/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.advice;

import java.util.ArrayList;

import io.mosip.captcha.util.ErrorConstants;
import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.util.CaptchaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.mosip.captcha.dto.ExceptionJSONInfoDTO;
import io.mosip.captcha.dto.ResponseWrapper;


@RestControllerAdvice
public class CaptchaExceptionHandler {

	@Autowired
	protected Environment env;

	@Value("${mosip.captcha.api.id}")
	public String captchaApiId;

	@Value("${mosip.captcha.api.version}")
	private String captchaApiVersion;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseWrapper<?>> handleInvalidCaptchaRequest(MethodArgumentNotValidException ex) {
		ResponseWrapper<?> response = new ResponseWrapper<>();
		response.setId(captchaApiId);
		response.setVersion(captchaApiVersion);
		response.setResponsetime(CaptchaUtils.getCurrentResponseTime());
		response.setResponse(null);
		ArrayList<ExceptionJSONInfoDTO> errors = new ArrayList<ExceptionJSONInfoDTO>();
		errors.add(new ExceptionJSONInfoDTO(ErrorConstants.INVALID_CAPTCHA_REQUEST, ErrorConstants.getErrorMessage(ErrorConstants.INVALID_CAPTCHA_REQUEST)));
		response.setErrors(errors);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ExceptionHandler(CaptchaException.class)
	public ResponseWrapper<?> handleCaptchaException(CaptchaException ex) {
		ResponseWrapper<?> response = new ResponseWrapper<>();
		response.setId(captchaApiId);
		response.setVersion(captchaApiVersion);
		response.setResponsetime(CaptchaUtils.getCurrentResponseTime());
		response.setResponse(null);
		ArrayList<ExceptionJSONInfoDTO> errors = new ArrayList<ExceptionJSONInfoDTO>();
		ExceptionJSONInfoDTO errorDetails = new ExceptionJSONInfoDTO(ex.getErrorCode(), ex.getErrorMessage());
		errors.add(errorDetails);
		response.setErrors(errors);
		return response;
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseWrapper<?> handleException(Exception ex) {
		ResponseWrapper<?> response = new ResponseWrapper<>();
		response.setId(captchaApiId);
		response.setVersion(captchaApiVersion);
		response.setResponsetime(CaptchaUtils.getCurrentResponseTime());
		response.setResponse(null);
		ArrayList<ExceptionJSONInfoDTO> errors = new ArrayList<ExceptionJSONInfoDTO>();
		ExceptionJSONInfoDTO errorDetails = new ExceptionJSONInfoDTO(ErrorConstants.CAPTCHA_VALIDATION_FAILED, ex.getMessage());
		errors.add(errorDetails);
		response.setErrors(errors);
		return response;
	}

}
