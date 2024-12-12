package io.mosip.captcha.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.RequestWrapper;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.exception.InvalidRequestCaptchaException;
import io.mosip.captcha.spi.CaptchaService;
import io.mosip.captcha.util.CaptchaErrorCode;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CaptchaController.class)
public class CaptchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaptchaService captchaService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void validateCaptcha_withValidRequest_returnSuccessResponse() throws Exception {
        RequestWrapper<CaptchaRequestDTO> requestWrapper = new RequestWrapper<>();
        CaptchaRequestDTO requestDTO = new CaptchaRequestDTO();
        requestDTO.setCaptchaToken("token");
        requestWrapper.setRequest(requestDTO);

        ResponseWrapper<CaptchaResponseDTO> responseWrapper = new ResponseWrapper<>();
        CaptchaResponseDTO response = new CaptchaResponseDTO();
        response.setSuccess(true);
        responseWrapper.setResponse(response);

        when(captchaService.validateCaptcha(any(CaptchaRequestDTO.class))).thenReturn(responseWrapper);

        mockMvc.perform(post("/validatecaptcha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWrapper))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.response.success").value(true));
    }

    @Test
    public void validateCaptcha_withInvalidRequest_returnErrorResponse() throws Exception {
        RequestWrapper<CaptchaRequestDTO> requestWrapper = new RequestWrapper<>();
        requestWrapper.setRequest(new CaptchaRequestDTO());

        when(captchaService.validateCaptcha(any(CaptchaRequestDTO.class))).thenThrow(new InvalidRequestCaptchaException(CaptchaErrorCode.INVALID_CAPTCHA_REQUEST.getErrorCode(), CaptchaErrorCode.INVALID_CAPTCHA_REQUEST.getErrorMessage()));

        mockMvc.perform(post("/validatecaptcha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWrapper))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", Matchers.hasSize(Matchers.greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.errors[0].errorCode", Matchers.is(CaptchaErrorCode.INVALID_CAPTCHA_REQUEST.getErrorCode())));
    }
}
