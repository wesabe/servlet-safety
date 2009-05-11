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

import com.wesabe.servlet.errors.ErrorReporter;

/**
 * A servlet filter which reports any unhandled exceptions raised by servlets
 * via an {@link ErrorReporter}.
 * 
 * @author coda
 */
public class ErrorReporterFilter implements Filter {
	private static final Logger LOGGER = Logger.getLogger(ErrorReporterFilter.class.getCanonicalName());
	private final ErrorReporter reporter;
	private final String errorMessage;
	
	/**
	 * Creates a new {@link ErrorReporterFilter}.
	 * 
	 * @param reporter the {@link ErrorReporter} to which exceptions will be reported
	 * @param errorMessage an additional message to be appended to the usual 500 response
	 */
	public ErrorReporterFilter(ErrorReporter reporter, String errorMessage) {
		this.reporter = reporter;
		this.errorMessage = errorMessage;
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		try {
			chain.doFilter(req, res);
		} catch (final Throwable e) {
			final String logMsg = String.format(
				"Unhandled exception raised while handling %s %s",
				request.getMethod(),
				request.getRequestURL()
			);
			LOGGER.log(Level.SEVERE, logMsg, e);
			
			response.reset();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			final PrintWriter writer = response.getWriter();
			writer.println("The server encountered an unexpected condition which prevented it from fulfilling the request.");
			writer.println();
			writer.println(errorMessage);
			reporter.report(request, response, e);
			writer.close();
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

}
