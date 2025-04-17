/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.dto;

import io.mosip.captcha.util.ErrorConstants;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CaptchaRequestDTO {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = ErrorConstants.INVALID_CAPTCHA_REQUEST)
	private String captchaToken;

	private String moduleName;
}
