package com.wesabe.servlet.normalizers;

import java.util.Locale;

import com.wesabe.servlet.normalizers.util.CaseInsensitiveSet;

/**
 * Normalizes HTTP request method names.
 * 
 * @author coda
 *
 */
public class MethodNormalizer implements Normalizer<String> {
	private static final Locale METHOD_LOCALE = Locale.US;
	private static final CaseInsensitiveSet METHOD_NAMES = CaseInsensitiveSet.of(METHOD_LOCALE, "GET", "PUT", "POST", "DELETE", "HEAD", "OPTIONS");
	
	@Override
	public String normalize(String method) throws ValidationException {
		if (METHOD_NAMES.contains(method)) {
			return method.toUpperCase(METHOD_LOCALE);
		}
		
		throw new ValidationException(method, "not a member of " + METHOD_NAMES);
	}

}
