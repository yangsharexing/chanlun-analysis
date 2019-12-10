package com.chalun.yx.Exception;

public class ZSException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String code;

	private String message;

	public ZSException(ExceptionCode excepton) {
		super();
		this.code = excepton.getCode();
		this.message = excepton.getMessage();
	}

	public ZSException() {
		super();
	}

	public ZSException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
