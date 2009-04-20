package com.wesabe.servlet;

import javax.servlet.http.HttpServletResponse;

/**
 * An unchecked exception which is raised by {@link SafeResponse} and other
 * classes to indicate that an outbound {@link HttpServletResponse} cannot be
 * sent as-is and the client should be notified as such.
 * 
 * @author coda
 *
 */
public class BadResponseException extends SecurityException {
	private static final long serialVersionUID = 222951643487079164L;
	private final HttpServletResponse response;
	
	/**
	 * Create a new {@link BadResponseException} for a response with a cause.
	 */
	public BadResponseException(HttpServletResponse response, Throwable cause) {
		super("Bad response", cause);
		this.response = response;
	}
	
	/**
	 * Return the bad response.
	 */
	public HttpServletResponse getBadResponse() {
		return response;
	}
}
