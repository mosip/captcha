/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
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
