package io.mosip.captcha.exception;

import io.mosip.captcha.util.ErrorConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaptchaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CaptchaException(String errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = ErrorConstants.getErrorMessage(errorCode);
	}

	public CaptchaException(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	private String errorCode;
	private String errorMessage;

}
