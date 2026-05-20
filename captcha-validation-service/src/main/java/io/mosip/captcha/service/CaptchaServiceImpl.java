/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.service;

import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.spi.CaptchaProvider;
import io.mosip.captcha.util.CaptchaProviderFactory;
import io.mosip.captcha.util.CaptchaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.spi.CaptchaService;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {

	@Value("${mosip.captcha.api.id}")
	private String captchaApiId;

	@Value("${mosip.captcha.api.version}")
	private String captchaApiVersion;

	@Value("${mosip.captcha.default.module-name:preregistration}")
	private String defaultModuleName;

	@Autowired
	private CaptchaProviderFactory captchaProviderFactory;

	@Override
	public ResponseWrapper<CaptchaResponseDTO> validateCaptcha(CaptchaRequestDTO captchaRequest) throws CaptchaException {
		String moduleName = Objects.requireNonNullElse(captchaRequest.getModuleName(), defaultModuleName);
		CaptchaProvider captchaProvider = captchaProviderFactory.getCaptchaProvider(moduleName);

		CaptchaResponseDTO response = captchaProvider.verifyCaptcha(moduleName, captchaRequest.getCaptchaToken().trim());

		ResponseWrapper<CaptchaResponseDTO> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(response);
		responseWrapper.setResponsetime(CaptchaUtils.getCurrentResponseTime());
		responseWrapper.setId(captchaApiId);
		responseWrapper.setVersion(captchaApiVersion);

		return responseWrapper;
	}

}
