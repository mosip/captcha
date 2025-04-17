/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.mosip.captcha.util.ErrorConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * This DTO class is used to define the initial request parameters.
 * 
 * @author Rajath KR
 * @author Akshay Jain
 * @since 1.0.0
 *
 */
@Getter
@Setter
@ToString
public class RequestWrapper<T> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4966448852014107698L;

	private String id;
	private String version;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private Date requesttime;

	@Valid
	@NotNull(message = ErrorConstants.INVALID_CAPTCHA_REQUEST)
	private T request;

	public Date getRequesttime() {
		return requesttime!=null ? new Date(requesttime.getTime()):null;
	}
	public void setRequesttime(Date requesttime) {
		this.requesttime =requesttime!=null ? new Date(requesttime.getTime()):null;
	}

}
