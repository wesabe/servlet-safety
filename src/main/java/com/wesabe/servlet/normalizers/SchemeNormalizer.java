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
	private final CaseInsensitiveSet SCHEMES = CaseInsensitiveSet.of(Locale.US, "HTTP", "HTTPS");
	
	@Override
	public String normalize(String scheme) throws ValidationException {
		if (SCHEMES.contains(scheme)) {
			return SCHEMES.lowercase(scheme);
		}
		
		throw new ValidationException(scheme, "not a member of " + SCHEMES);
	}

}
