package com.wesabe.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.wesabe.servlet.normalizers.*;

public class SafeResponse extends HttpServletResponseWrapper {
	private static final HeaderNameNormalizer HEADER_NAME_NORMALIZER = new HeaderNameNormalizer();
	private static final HeaderValueNormalizer HEADER_VALUE_NORMALIZER = new HeaderValueNormalizer();
	private static final CookieNormalizer COOKIE_NORMALIZER = new CookieNormalizer();
	private final HttpServletResponse response;

	public SafeResponse(HttpServletResponse response) {
		super(response);
		this.response = response;
	}

	@Override
	public void addCookie(Cookie cookie) {
		try {
			super.addCookie(COOKIE_NORMALIZER.normalize(cookie));
		} catch (ValidationException e) {
			throw new BadResponseException(response, e);
		}
	}

	@Override
	public void addHeader(String name, String value) {
		try {
			super.addHeader(
				HEADER_NAME_NORMALIZER.normalize(name),
				HEADER_VALUE_NORMALIZER.normalize(value)
			);
		} catch (ValidationException e) {
			throw new BadResponseException(response, e);
		}
	}

	@Override
	public void addDateHeader(String name, long date) {
		try {
			super.addDateHeader(HEADER_NAME_NORMALIZER.normalize(name), date);
		} catch (ValidationException e) {
			throw new BadResponseException(response, e);
		}
	}

	@Override
	public void addIntHeader(String name, int value) {
		try {
			super.addIntHeader(HEADER_NAME_NORMALIZER.normalize(name), value);
		} catch (ValidationException e) {
			throw new BadResponseException(response, e);
		}
	}

	@Override
	public void setHeader(String name, String value) {
		try {
			super.setHeader(
				HEADER_NAME_NORMALIZER.normalize(name),
				HEADER_VALUE_NORMALIZER.normalize(value)
			);
		} catch (ValidationException e) {
			throw new BadResponseException(response, e);
		}
	}

	@Override
	public void setDateHeader(String name, long date) {
		try {
			super.setDateHeader(HEADER_NAME_NORMALIZER.normalize(name), date);
		} catch (ValidationException e) {
			throw new BadResponseException(response, e);
		}
	}

	@Override
	public void setIntHeader(String name, int value) {
		try {
			super.setIntHeader(HEADER_NAME_NORMALIZER.normalize(name), value);
		} catch (ValidationException e) {
			throw new BadResponseException(response, e);
		}
	}

	@Override
	public String encodeRedirectUrl(String url) {
		return url;
	}

	@Override
	public String encodeRedirectURL(String url) {
		return url;
	}

	@Override
	public String encodeUrl(String url) {
		return url;
	}

	@Override
	public String encodeURL(String url) {
		return url;
	}

}
