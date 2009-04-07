package com.wesabe.servlet.normalizers;

import com.wesabe.servlet.normalizers.util.CharacterSet;

/**
 * Normalizes hostnames.
 * 
 * @author coda
 */
public class HostnameNormalizer implements Normalizer<String> {
	private static final String INVALID_SEPARATORS = "..";
	private static final CharacterSet VALID_CHARACTERS = CharacterSet.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_.-");
	
	@Override
	public String normalize(String hostname) throws ValidationException {
		if (VALID_CHARACTERS.composes(hostname) && !hostname.contains(INVALID_SEPARATORS)) {
			return hostname;
		}
		
		throw new ValidationException(hostname, "not a valid hostname");
	}

}
