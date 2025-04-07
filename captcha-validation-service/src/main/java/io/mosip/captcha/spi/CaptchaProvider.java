package io.mosip.captcha.spi;

import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.exception.CaptchaException;

public interface CaptchaProvider {
    ResponseWrapper<CaptchaResponseDTO> verifyCaptcha(String moduleName, String captchaToken) throws CaptchaException;
}
