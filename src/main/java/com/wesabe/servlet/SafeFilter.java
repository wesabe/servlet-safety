package com.wesabe.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SafeFilter implements Filter {
	private static final Logger LOGGER = Logger.getLogger(SafeFilter.class.getCanonicalName());
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		final HttpServletRequest request = (HttpServletRequest) req;
		final SafeRequest safeRequest = new SafeRequest(request);
		final HttpServletResponse response = (HttpServletResponse) resp;
		final SafeResponse safeResponse = new SafeResponse(response);
		
		try {
			chain.doFilter(safeRequest, safeResponse);
		} catch (BadRequestException e) {
			logBadRequest(request, e);
			writeErrorMessage(
				response,
				HttpServletResponse.SC_BAD_REQUEST,
				"The request could not be understood by the server due to malformed syntax. Do not re-send this request without modifications."
			);
		} catch (BadResponseException e) {
			logBadResponse(request, response, e);
			writeErrorMessage(
				response,
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				"The server encountered an unexpected condition which prevented it from fulfilling the request."
			);
		}
	}

	private void logBadResponse(HttpServletRequest request, HttpServletResponse response,
			BadResponseException e) {
		final StringBuilder builder = new StringBuilder();
		builder.append("Bad response to ");
		builder.append(request.getMethod()).append(" ").append(request.getRequestURI());
		if (request.getQueryString() != null) {
			builder.append("?").append(request.getQueryString());
		}
		builder.append(" received:\n\n");
		builder.append("======\n");
		builder.append(response.toString());
		builder.append("\n======");
		LOGGER.log(Level.SEVERE, builder.toString(), e);
	}

	private void logBadRequest(HttpServletRequest request, BadRequestException e) {
		final StringBuilder builder = new StringBuilder();
		builder.append("Bad request received:\n\n");
		builder.append("======\n");
		builder.append(request.toString());
		builder.append("\n======");
		LOGGER.log(Level.SEVERE, builder.toString(), e);
	}

	private void writeErrorMessage(HttpServletResponse response, int status, String message)
			throws IOException {
		response.reset();
		response.setStatus(status);
		final PrintWriter writer = response.getWriter();
		writer.println(message);
		writer.close();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

}
