package io.mosip.captcha.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GoogleReCaptchaV2Response implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("success")
	private boolean success;

	@JsonProperty("challenge_ts")
	private String challengeTs;

	@JsonProperty("hostname")
	private String hostname;
	
    @JsonProperty("error-codes")
	private List<String> errorCodes;

}
