package com.wesabe.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.google.common.collect.Lists;
import com.wesabe.servlet.normalizers.*;

public class SafeRequest extends HttpServletRequestWrapper {
	private static final String REQUEST_DISPATCHER_PATH_PREFIX = "WEB-INF";
	private static final MethodNormalizer METHOD_NORMALIZER = new MethodNormalizer();
	private static final SchemeNormalizer SCHEME_NORMALIZER = new SchemeNormalizer();
	private static final PortNormalizer PORT_NORMALIZER = new PortNormalizer();
	private static final HostnameNormalizer HOSTNAME_NORMALIZER = new HostnameNormalizer();
	private static final HeaderNameNormalizer HEADER_NAME_NORMALIZER = new HeaderNameNormalizer();
	private static final HeaderValueNormalizer HEADER_VALUE_NORMALIZER = new HeaderValueNormalizer();
	private static final CookieNormalizer COOKIE_NORMALIZER = new CookieNormalizer();
	private static final UriNormalizer URI_NORMALIZER = new UriNormalizer();
	private static final QueryStringNormalizer QUERY_STRING_NORMALIZER = new QueryStringNormalizer();
	private static final ParameterNameNormalizer PARAM_NAME_NORMALIZER = new ParameterNameNormalizer();
	
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
		try {
			final Cookie[] cookies = super.getCookies();
			if (cookies == null) {
				return new Cookie[0];
			}
			
			for (int i = 0; i < cookies.length; i++) {
				cookies[i] = COOKIE_NORMALIZER.normalize(cookies[i]);
			}
			
			return cookies;
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
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
		try {
			final String validName = getValidHeaderName(name);
			
			return HEADER_VALUE_NORMALIZER.normalize(super.getHeader(validName));
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
	}
	
	@Override
	public Enumeration<String> getHeaderNames() {
		try {
			final List<String> names = Lists.newLinkedList();
			final Enumeration<?> rawNames = super.getHeaderNames();
			while (rawNames.hasMoreElements()) {
				names.add(HEADER_NAME_NORMALIZER.normalize((String) rawNames.nextElement()));
			}
			return Collections.enumeration(names);
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
	}
	
	@Override
	public Enumeration<String> getHeaders(String name) {
		try {
			final List<String> values = Lists.newLinkedList();
			final String validName = getValidHeaderName(name);
			
			final Enumeration<?> rawValues = super.getHeaders(validName);
			while (rawValues.hasMoreElements()) {
				String rawValue = (String) rawValues.nextElement();
				values.add(HEADER_VALUE_NORMALIZER.normalize(rawValue));
			}
			
			return Collections.enumeration(values);
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
	}

	private String getValidHeaderName(String name) {
		final String validName;
		try {
			validName = HEADER_NAME_NORMALIZER.normalize(name);
		} catch (ValidationException e) {
			throw new IllegalArgumentException(e);
		}
		return validName;
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
		try {
			final List<String> names = Lists.newLinkedList();
			final Enumeration<?> rawNames = super.getParameterNames();
			while (rawNames.hasMoreElements()) {
				names.add(PARAM_NAME_NORMALIZER.normalize((String) rawNames.nextElement()));
			}
			return Collections.enumeration(names);
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
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
		try {
			return QUERY_STRING_NORMALIZER.normalize(super.getQueryString());
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
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
		try {
			return URI_NORMALIZER.normalize(super.getRequestURI());
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
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
