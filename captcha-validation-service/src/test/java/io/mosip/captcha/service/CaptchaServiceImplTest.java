/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.service;

import static org.junit.Assert.*;

import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.spi.CaptchaProvider;
import io.mosip.captcha.util.CaptchaProviderFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class CaptchaServiceImplTest {

	@InjectMocks
	private CaptchaServiceImpl captchaServiceImpl;

	@Mock
	private CaptchaProviderFactory captchaProviderFactory;

	@Mock
	private CaptchaProvider captchaProvider;

	@Before
	public void setUp() {
		ReflectionTestUtils.setField(captchaServiceImpl, "captchaApiId", "123");
		ReflectionTestUtils.setField(captchaServiceImpl, "captchaApiVersion", "2.0");
		Mockito.when(captchaProviderFactory.getCaptchaProvider(Mockito.any(String.class))).thenReturn(captchaProvider);
	}

	@Test
	public void validateCaptcha_withValidInput_thenPass() throws CaptchaException {
		CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
		captchaRequest.setModuleName("module");
		captchaRequest.setCaptchaToken("captcha_token");

		CaptchaResponseDTO response = new CaptchaResponseDTO();
		response.setSuccess(true);

		Mockito.when(captchaProvider.verifyCaptcha(Mockito.anyString(), Mockito.anyString())).thenReturn(response);
		ResponseWrapper<CaptchaResponseDTO> responseWrapper = captchaServiceImpl.validateCaptcha(captchaRequest);
		assertNotNull(responseWrapper);
		assertNotNull(responseWrapper.getResponse());
		assertTrue(responseWrapper.getResponse().isSuccess());
	}

	@Test(expected = CaptchaException.class)
	public void validateCaptcha_withInvalidToken_thenException() throws CaptchaException {
		CaptchaRequestDTO captchaRequest = new CaptchaRequestDTO();
		captchaRequest.setModuleName("module");
		captchaRequest.setCaptchaToken("invalid_captcha_token");

		Mockito.when(captchaProvider.verifyCaptcha(Mockito.anyString(), Mockito.anyString())).thenThrow(new CaptchaException("Invalid captcha token"));
		captchaServiceImpl.validateCaptcha(captchaRequest);
	}

}
