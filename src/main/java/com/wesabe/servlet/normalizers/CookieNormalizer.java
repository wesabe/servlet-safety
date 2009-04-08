package com.wesabe.servlet.normalizers;

import javax.servlet.http.Cookie;

import com.wesabe.servlet.normalizers.util.CharacterSet;

/**
 * Normalizes HTTP cookies.
 * 
 * @author coda
 */
public class CookieNormalizer implements Normalizer<Cookie> {
	private static class CookieValueNormalizer implements Normalizer<String> {
		private static final CharacterSet VALID_CHARACTERS = CharacterSet.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-/+=_ ");
		
		@Override
		public String normalize(String input) throws ValidationException {
			if (VALID_CHARACTERS.composes(input)) {
				return input;
			}
			
			throw new ValidationException(input, "not a valid cookie value");
		}
	}
	
	private static class CookieNameNormalizer implements Normalizer<String> {
		private static final CharacterSet VALID_CHARACTERS = CharacterSet.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-");
		
		@Override
		public String normalize(String name) throws ValidationException {
			if (VALID_CHARACTERS.composes(name)) {
				return name;
			}
			
			throw new ValidationException(name, "not a valid cookie name");
		}
	}
	
	public static class CookiePathNormalizer implements Normalizer<String> {
		private static final CharacterSet VALID_CHARACTERS = CharacterSet.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789()-=*.?;,+/:&_ ");
		
		@Override
		public String normalize(String value) throws ValidationException {
			if (VALID_CHARACTERS.composes(value)) {
				return value;
			}
			
			throw new ValidationException(value, "not a valid cookie path");
		}

	}
	
	private static final CookieNameNormalizer NAME_NORMALIZER = new CookieNameNormalizer();
	private static final CookieValueNormalizer VALUE_NORMALIZER = new CookieValueNormalizer();
	private static final HostnameNormalizer DOMAIN_NORMALIZER = new HostnameNormalizer();
	private static final CookiePathNormalizer PATH_NORMALIZER = new CookiePathNormalizer();
	
	@Override
	public Cookie normalize(Cookie cookie) throws ValidationException {
		final Cookie safeCookie = new Cookie(
			NAME_NORMALIZER.normalize(cookie.getName()),
			VALUE_NORMALIZER.normalize(cookie.getValue())
		);
		
		// safe attributes
		safeCookie.setMaxAge(cookie.getMaxAge());
		safeCookie.setSecure(cookie.getSecure());
		safeCookie.setVersion(cookie.getVersion());
		
		// unsafe attributes
		safeCookie.setDomain(DOMAIN_NORMALIZER.normalize(cookie.getDomain()));
		safeCookie.setPath(PATH_NORMALIZER.normalize(cookie.getPath()));
		
		// REVIEW coda@wesabe.com -- Apr 8, 2009: Should we be dropping cookie comments?
		
		return safeCookie;
	}

}
