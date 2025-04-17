/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Akshay Jain
 * @since 1.0.0
 *
 * @param <T>
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResponseWrapper<T> implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3384945682672832638L;
	/**
	 * Id
	 */
	private String id;
	/**
	 * version
	 */
	private String version;

	private String responsetime;

	private T response;
	
	/** The error details. */
	private List<ExceptionJSONInfoDTO> errors;
}
