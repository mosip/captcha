package io.mosip.captcha.spi;

import io.mosip.captcha.exception.CaptchaException;
import io.mosip.captcha.exception.InvalidRequestCaptchaException;

public interface CaptchaService{

	Object validateCaptcha(Object captchaRequest) throws CaptchaException, InvalidRequestCaptchaException;


}
