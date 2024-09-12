package com.catpang.core.codes;

import org.springframework.http.HttpStatus;

public interface ResponseCode {
	HttpStatus getStatus();

	String getMessage();

	String getDivisionCode();
}