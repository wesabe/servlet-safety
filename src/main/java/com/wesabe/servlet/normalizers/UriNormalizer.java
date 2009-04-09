package com.wesabe.servlet.normalizers;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Normalizes URIs.
 * 
 * @author coda
 */
public class UriNormalizer extends UriFragmentNormalizer {
	private static final char PATH_SEPARATOR = '/';
	
	/**
	 * First, asserts that {@code uri} is composed of valid characters. Second,
	 * breaks the URI up into path components (e.g., {@code /one/two/three}) and
	 * normalizes them as fragments.
	 * 
	 * @see UriFragmentNormalizer
	 */
	@Override
	public String normalize(String uri) throws ValidationException {
		if (isWellFormed(uri)) {
			try {
				StringBuilder pathBuilder = new StringBuilder(uri.length() * 2);
				StringBuilder buffer = new StringBuilder(uri.length());
				
				for (int i = 0; i < uri.length(); i++) {
					final char c = uri.charAt(i);
					if (c == PATH_SEPARATOR) {
						pathBuilder.append(super.normalize(buffer.toString()));
						pathBuilder.append(c);
						buffer = new StringBuilder(uri.length());
					} else {
						buffer.append(c);
					}
				}
				
				if (buffer.length() > 0) {
					pathBuilder.append(super.normalize(buffer.toString()));
				}
				
				return pathBuilder.toString();
			} catch (ValidationException e) {
				throw new ValidationException(uri, e.getMessage());
			}
		}
		
		throw new ValidationException(uri, "not a valid URI");
	}

	private boolean isWellFormed(final String uri) {
		try {
			new URI(uri).getRawPath();
			return true;
		} catch (URISyntaxException e) {
			return false;
		}
	}

}
