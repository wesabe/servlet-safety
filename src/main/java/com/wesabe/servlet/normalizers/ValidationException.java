package com.wesabe.servlet.normalizers;

/**
 * An exception raised when an input or output value is invalid and cannot be
 * normalized.
 * 
 * @author coda
 */
public class ValidationException extends Exception {
	private static final long serialVersionUID = 660798815590999921L;
	private final Object value;

	public ValidationException(Object value, String reason) {
		super("Invalid value: " + value + " (" + reason + ")");
		this.value = value;
	}

	public ValidationException(Object value, Throwable cause) {
		super("Invalid value: " + value + " (" + cause.getMessage() + ")", cause);
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
}
