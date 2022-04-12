package com.app.exception;

public class CartEmptyException extends NullPointerException {

	public CartEmptyException() {
		super();
	}

	public CartEmptyException(String msg) {
		super(msg);
	}
}
