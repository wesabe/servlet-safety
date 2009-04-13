package com.wesabe.servlet.normalizers;

import com.wesabe.servlet.normalizers.util.CharacterSet;

/**
 * Normalizes session IDs.
 * 
 * @author coda
 */
public class SessionIdNormalizer implements Normalizer<String> {
	private static final CharacterSet VALID_CHARACTERS = CharacterSet.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
	private static final int MIN_SESSION_ID_LENGTH = 10;
	private static final int MAX_SESSION_ID_LENGTH = 30;

	@Override
	public String normalize(String input) throws ValidationException {
		if (VALID_CHARACTERS.composes(input) && validLength(input)) {
			return input;
		}
		
		throw new ValidationException(input, "not a valid session ID");
	}

	private boolean validLength(String input) {
		return input.length() >= MIN_SESSION_ID_LENGTH && input.length() <= MAX_SESSION_ID_LENGTH;
	}

}
