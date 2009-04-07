package com.wesabe.servlet.normalizers;

import com.wesabe.servlet.normalizers.util.CharacterSet;

/**
 * Normalizes HTTP header field names.
 * 
 * @author coda
 */
public class HeaderNameNormalizer implements Normalizer<String> {
	private static final CharacterSet VALID_CHARACTERS = CharacterSet.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-");
	
	@Override
	public String normalize(String name) throws ValidationException {
		if (VALID_CHARACTERS.composes(name)) {
			return name;
		}
		
		throw new ValidationException(name, "not a valid HTTP header");
	}
}
