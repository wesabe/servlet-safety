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
	private static final CaseInsensitiveSet METHODS = CaseInsensitiveSet.of(
		Locale.US,
		"GET", "PUT", "POST", "DELETE", "HEAD", "OPTIONS"
	);
	
	@Override
	public String normalize(String method) throws ValidationException {
		if (METHODS.contains(method)) {
			return METHODS.uppercase(method);
		}
		
		throw new ValidationException(method, "not a member of " + METHODS);
	}

}
