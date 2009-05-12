package com.wesabe.servlet.normalizers;

import com.wesabe.servlet.normalizers.util.CharacterSet;

/**
 * Normalizes HTTP header values.
 * 
 * @author coda
 */
public class HeaderValueNormalizer implements Normalizer<String> {
	private static final CharacterSet VALID_CHARACTERS = CharacterSet.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789()-=*.?;,+/:&_ \"");
	
	@Override
	public String normalize(String value) throws ValidationException {
		if (value == null) {
			return value;
		}
		
		if (VALID_CHARACTERS.composes(value)) {
			return value;
		}
		
		throw new ValidationException(value, "not a valid HTTP header value");
	}

}
