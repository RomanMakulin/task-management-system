package com.wayz.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

import java.nio.charset.Charset;

public class UserNotFoundException extends HttpStatusCodeException {

    protected UserNotFoundException(HttpStatusCode statusCode) {
        super(statusCode);
    }

    public UserNotFoundException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    protected UserNotFoundException(HttpStatusCode statusCode, String statusText, byte[] responseBody, Charset responseCharset) {
        super(statusCode, statusText, responseBody, responseCharset);
    }

    protected UserNotFoundException(HttpStatusCode statusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
        super(statusCode, statusText, responseHeaders, responseBody, responseCharset);
    }

    protected UserNotFoundException(String message, HttpStatusCode statusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
        super(message, statusCode, statusText, responseHeaders, responseBody, responseCharset);
    }

}
