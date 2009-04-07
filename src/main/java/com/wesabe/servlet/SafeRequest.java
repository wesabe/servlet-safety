package com.wesabe.servlet;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.wesabe.servlet.normalizers.HostnameNormalizer;
import com.wesabe.servlet.normalizers.MethodNormalizer;
import com.wesabe.servlet.normalizers.PortNormalizer;
import com.wesabe.servlet.normalizers.SchemeNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

public class SafeRequest extends HttpServletRequestWrapper {
	private static final String REQUEST_DISPATCHER_PATH_PREFIX = "WEB-INF";
	private static final MethodNormalizer METHOD_NORMALIZER = new MethodNormalizer();
	private static final SchemeNormalizer SCHEME_NORMALIZER = new SchemeNormalizer();
	private static final PortNormalizer PORT_NORMALIZER = new PortNormalizer();
	private static final HostnameNormalizer HOSTNAME_NORMALIZER = new HostnameNormalizer();
	
	private final HttpServletRequest request;
	
	public SafeRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
	}
	
	@Override
	public String getContextPath() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize context path
		// path =~ ^[a-zA-Z0-9.\\-_]$
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Cookie[] getCookies() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize cookies
		// name =~ ^[a-zA-Z0-9\\-_]{0,32}$
		// value =~ ^[a-zA-Z0-9\\-\\/+=_ ]*$
		// domain = hostname
		// path = HTTP header value
		throw new UnsupportedOperationException();
	}
	
	@Override
	public long getDateHeader(String name) {
		try {
			return super.getDateHeader(name);
		} catch (IllegalArgumentException e) {
			throw new BadRequestException(request, e);
		}
	}
	
	@Override
	public String getHeader(String name) {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize header values
		// value =~ ^[a-zA-Z0-9()\\-=\\*\\.\\?;,+\\/:&_ ]*$
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Enumeration<String> getHeaderNames() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize header names
		// name =~ ^[a-zA-Z0-9\\-_]{0,32}$
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Enumeration<String> getHeaders(String name) {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize header values
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getIntHeader(String name) {
		try {
			return super.getIntHeader(name);
		} catch (IllegalArgumentException e) {
			throw new BadRequestException(request, e);
		}
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
		// param value = ^[a-zA-Z0-9.\\-\\/+=_ ]*$
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize parameter values
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize parameter names
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Enumeration<String> getParameterNames() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize parameter names
		// param name =~ ^[a-zA-Z0-9_]{0,32}$
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
		// path =~ ^[a-zA-Z0-9.\\-_]$
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getQueryString() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize query string
		// split by '&', split by '=', URI decode/encode
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		if (path.startsWith(REQUEST_DISPATCHER_PATH_PREFIX)) {
            return request.getRequestDispatcher(path);
        }
		
        return null;
	}
	
	@Override
	public String getRequestedSessionId() {
		// TODO coda@wesabe.com -- Apr 6, 2009: sanitize session id
		// id =~ ^[A-Z0-9]{10,30}$
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
		try {
			return SCHEME_NORMALIZER.normalize(super.getScheme());
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
	}
	
	@Override
	public String getServerName() {
		try {
			return HOSTNAME_NORMALIZER.normalize(super.getServerName());
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
	}
	
	@Override
	public int getServerPort() {
		try {
			return PORT_NORMALIZER.normalize(super.getServerPort());
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
	}
	
	@Override
	public String getServletPath() {
		// REVIEW coda@wesabe.com -- Apr 6, 2009: Figure out what servlet path normalization means
		throw new UnsupportedOperationException();
	}
}
