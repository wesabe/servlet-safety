package com.wesabe.servlet.normalizers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Normalizes URI fragments.
 * 
 * @author coda
 */
public abstract class UriFragmentNormalizer implements Normalizer<String> {
	private static final int MAX_DECODE_DEPTH = 1;
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final char REPLACEMENT_CHARACTER = '\uFFFD';

	@Override
	public String normalize(String fragment) throws ValidationException {
		try {
			String lastDecoded = fragment;
			for (int i = MAX_DECODE_DEPTH; i >= 0; i--) {
				final String decoded = URLDecoder.decode(lastDecoded, DEFAULT_CHARSET);
				
				if (decoded.indexOf(REPLACEMENT_CHARACTER) != -1) {
					throw new ValidationException(fragment, "cannot contain invalid UTF-8 codepoints");
				}
				
				if (lastDecoded.equals(decoded)) {
					return URLEncoder.encode(lastDecoded, DEFAULT_CHARSET);
				}
				
				lastDecoded = decoded;
			}
			
			throw new ValidationException(fragment, "was encoded " + MAX_DECODE_DEPTH + " or more times");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new ValidationException(fragment, "had un-decodable characters");
		}
	}

}
