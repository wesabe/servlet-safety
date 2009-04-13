package com.wesabe.servlet.normalizers;

import com.wesabe.servlet.normalizers.util.CharacterSet;

/**
 * Normalizes servlet parameter names.
 * 
 * @author coda
 */
public class ParameterValueNormalizer implements Normalizer<String> {
	private static final CharacterSet VALID_CHARACTERS = CharacterSet.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_- +=./");

	@Override
	public String normalize(String input) throws ValidationException {
		if (VALID_CHARACTERS.composes(input)) {
			return input;
		}
		
		throw new ValidationException(input, "not a valid parameter value");
	}

}
