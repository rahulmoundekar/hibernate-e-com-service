package com.app.exception;

import java.util.InputMismatchException;

public class WrongInputException extends InputMismatchException {

	public WrongInputException() {
		super();
	}

	public WrongInputException(String msg) {
		super(msg);
	}
}
