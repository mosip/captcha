/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.exception;

import java.util.List;

import io.mosip.captcha.dto.ExceptionJSONInfoDTO;
import io.mosip.captcha.dto.ResponseWrapper;
import lombok.Getter;
import lombok.Setter;

/**
 * @author M1046129
 *
 */

@Getter
@Setter
public class InvalidRequestParameterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3898906527162403384L;
	
	private ResponseWrapper<?> responseWrapper;
	private List<ExceptionJSONInfoDTO> exptionList;
	private String operation;
	
	public InvalidRequestParameterException() {
		super();
	}

	public InvalidRequestParameterException(String errCode, String errMessage, ResponseWrapper<?> response) {
		this.responseWrapper =response;
	}
	public InvalidRequestParameterException(String errorCode, String errorMessage, Throwable rootCause, ResponseWrapper<?> response) {
		this.responseWrapper =response;
	}
	
	
	public InvalidRequestParameterException(List<ExceptionJSONInfoDTO> exptionList, ResponseWrapper<?> response) {
		this.responseWrapper =response;
		this.exptionList=exptionList;
	}
	
	public InvalidRequestParameterException(List<ExceptionJSONInfoDTO> exptionList, String operation, ResponseWrapper<?> response) {
		this.responseWrapper =response;
		this.exptionList=exptionList;
		this.operation=operation;
	}
}
