/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.captcha.dto.CaptchaRequestDTO;
import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.dto.RequestWrapper;
import io.mosip.captcha.dto.ResponseWrapper;
import io.mosip.captcha.spi.CaptchaService;
import io.mosip.captcha.util.ErrorConstants;
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
    public void testValidateCaptchaEndpoint_withValidRequest_thenPass() throws Exception {
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
    public void testValidateCaptchaEndpoint_withInvalidRequest_thenError() throws Exception {
        RequestWrapper<CaptchaRequestDTO> requestWrapper = new RequestWrapper<>();
        requestWrapper.setRequest(new CaptchaRequestDTO());

        mockMvc.perform(post("/validatecaptcha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWrapper))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].errorCode").value(ErrorConstants.INVALID_CAPTCHA_REQUEST));
    }
}
