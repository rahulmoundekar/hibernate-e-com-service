package com.app.exception;

public class ProductNotAvailableException extends Exception {
	public ProductNotAvailableException() {
		super();
	}

	public ProductNotAvailableException(String msg) {
		super(msg);
	}

}
