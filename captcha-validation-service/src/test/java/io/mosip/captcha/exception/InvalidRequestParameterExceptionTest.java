package io.mosip.captcha.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import io.mosip.captcha.dto.ExceptionJSONInfoDTO;
import io.mosip.captcha.dto.ResponseWrapper;

public class InvalidRequestParameterExceptionTest {

    @Test
    public void testDefaultConstructor() {
        InvalidRequestParameterException ex = new InvalidRequestParameterException();
        assertNotNull(ex);
    }

    @Test
    public void testConstructorWithErrorCodeAndMessage() {
        ResponseWrapper<String> response = new ResponseWrapper<>();
        InvalidRequestParameterException ex = new InvalidRequestParameterException("ERR001", "Error message", response);

        assertEquals(response, ex.getResponseWrapper());
    }

    @Test
    public void testConstructorWithErrorCodeMessageAndThrowable() {
        ResponseWrapper<String> response = new ResponseWrapper<>();
        Throwable cause = new Throwable();
        InvalidRequestParameterException ex = new InvalidRequestParameterException("ERR002", "Error message", cause, response);

        assertEquals(response, ex.getResponseWrapper());
    }

    @Test
    public void testConstructorWithExceptionList() {
        List<ExceptionJSONInfoDTO> exceptionList = new ArrayList<>();
        ResponseWrapper<String> response = new ResponseWrapper<>();
        InvalidRequestParameterException ex = new InvalidRequestParameterException(exceptionList, response);

        assertEquals(response, ex.getResponseWrapper());
        assertEquals(exceptionList, ex.getExptionList());
    }

    @Test
    public void testConstructorWithExceptionListAndOperation() {
        List<ExceptionJSONInfoDTO> exceptionList = new ArrayList<>();
        String operation = "CREATE";
        ResponseWrapper<String> response = new ResponseWrapper<>();
        InvalidRequestParameterException ex = new InvalidRequestParameterException(exceptionList, operation, response);

        assertEquals(response, ex.getResponseWrapper());
        assertEquals(exceptionList, ex.getExptionList());
        assertEquals(operation, ex.getOperation());
    }

    @Test
    public void testSettersAndGetters() {
        InvalidRequestParameterException ex = new InvalidRequestParameterException();
        ResponseWrapper<String> response = new ResponseWrapper<>();
        List<ExceptionJSONInfoDTO> exceptionList = new ArrayList<>();
        String operation = "UPDATE";

        ex.setResponseWrapper(response);
        ex.setExptionList(exceptionList);
        ex.setOperation(operation);

        assertEquals(response, ex.getResponseWrapper());
        assertEquals(exceptionList, ex.getExptionList());
        assertEquals(operation, ex.getOperation());
    }
}
