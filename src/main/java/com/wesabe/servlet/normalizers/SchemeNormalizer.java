package com.wesabe.servlet.normalizers;

import java.util.Locale;

import com.wesabe.servlet.normalizers.util.CaseInsensitiveSet;

/**
 * Normalizes URI schemes.
 * 
 * @author coda
 *
 */
public class SchemeNormalizer implements Normalizer<String> {
	private final Locale SCHEME_LOCALE = Locale.US;
	private final CaseInsensitiveSet SCHEMES = CaseInsensitiveSet.of(SCHEME_LOCALE, "HTTP", "HTTPS");
	
	@Override
	public String normalize(String scheme) throws ValidationException {
		if (SCHEMES.contains(scheme)) {
			return scheme.toLowerCase(SCHEME_LOCALE);
		}
		
		throw new ValidationException(scheme, "not a member of " + SCHEMES);
	}

}
