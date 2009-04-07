package com.wesabe.servlet;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import com.wesabe.servlet.normalizers.MethodNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

public class SafeRequest extends HttpServletRequestWrapper {
	private static final MethodNormalizer METHOD_NORMALIZER = new MethodNormalizer();
	
	private final HttpServletRequest request;
	
	public SafeRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
	}
	
	@Override
	public String getContextPath() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize context path
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Cookie[] getCookies() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize cookies
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getHeader(String name) {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize header values
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Enumeration<?> getHeaderNames() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize header names
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Enumeration<?> getHeaders(String name) {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize header values
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getMethod() {
		try {
			return METHOD_NORMALIZER.normalize(super.getMethod());
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
	}
	
	@Override
	public String getParameter(String name) {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize parameter values
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Map<?, ?> getParameterMap() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize parameter values
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize parameter names
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Enumeration<?> getParameterNames() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize parameter names
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String[] getParameterValues(String name) {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize parameter values
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getPathInfo() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize path info
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getQueryString() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize query string
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize request dispatcher
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getRequestedSessionId() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize session id
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getRequestURI() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize request uri
		throw new UnsupportedOperationException();
	}
	
	@Override
	public StringBuffer getRequestURL() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize request url
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getScheme() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize scheme
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getServerName() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize server name
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getServerPort() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize server port
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getServletPath() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize servlet path
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	public HttpSession getSession() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize session
		throw new UnsupportedOperationException();
	}
	
	@Override
	public HttpSession getSession(boolean create) {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize session
		throw new UnsupportedOperationException();
	}
}
