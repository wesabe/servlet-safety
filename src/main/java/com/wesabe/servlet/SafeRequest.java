package com.wesabe.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
	private static final ParameterValueNormalizer PARAM_VALUE_NORMALIZER = new ParameterValueNormalizer();
	private static final SessionIdNormalizer SESSION_ID_NORMALIZER = new SessionIdNormalizer();
	private static final PathInfoNormalizer PATH_INFO_NORMALIZER = new PathInfoNormalizer();
	
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
		try {
			return HEADER_NAME_NORMALIZER.normalize(name);
		} catch (ValidationException e) {
			throw new IllegalArgumentException(e);
		}
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
		final String validName = getValidParameterName(name);
		
		try {
			return PARAM_VALUE_NORMALIZER.normalize(super.getParameter(validName));
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
	}

	private String getValidParameterName(String name) {
		try {
			return PARAM_NAME_NORMALIZER.normalize(name);
		} catch (ValidationException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {
		try {
			final Map<?, ?> rawMap = super.getParameterMap();
			final Map<String, String[]> map = Maps.newLinkedHashMap();
			for (Entry<?, ?> parameter : rawMap.entrySet()) {
				final String validName = PARAM_NAME_NORMALIZER.normalize((String) parameter.getKey());
				final String[] values = (String[]) parameter.getValue();
				for (int i = 0; i < values.length; i++) {
					values[i] = PARAM_VALUE_NORMALIZER.normalize(values[i]);
				}
				map.put(validName, values);
			}
			return map;
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
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
		try {
			final String[] values = super.getParameterValues(getValidParameterName(name));
			for (int i = 0; i < values.length; i++) {
				values[i] = PARAM_VALUE_NORMALIZER.normalize(values[i]);
			}
			return values;
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
	}
	
	@Override
	public String getPathInfo() {
		try {
			return PATH_INFO_NORMALIZER.normalize(super.getPathInfo());
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
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
		try {
			return SESSION_ID_NORMALIZER.normalize(super.getRequestedSessionId());
		} catch (ValidationException e) {
			throw new BadRequestException(request, e);
		}
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
		// REVIEW coda@wesabe.com -- Apr 13, 2009: Figure out how best to filter HttpServletRequest#getRequestURL().
		// ESAPI just punts on the issue -- should we automatically assemble it ourselves?
		return super.getRequestURL();
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
		return super.getServletPath();
	}
}
