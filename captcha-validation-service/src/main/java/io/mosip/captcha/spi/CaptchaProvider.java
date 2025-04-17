package io.mosip.captcha.spi;

import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.exception.CaptchaException;

public interface CaptchaProvider {

    String getProviderName();

    CaptchaResponseDTO verifyCaptcha(String moduleName, String captchaToken) throws CaptchaException;

}
