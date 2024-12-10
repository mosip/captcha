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
