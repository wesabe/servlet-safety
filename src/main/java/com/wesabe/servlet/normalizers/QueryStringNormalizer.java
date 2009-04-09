package com.wesabe.servlet.normalizers;

/**
 * Normalizes query strings.
 * 
 * @author coda
 */
public class QueryStringNormalizer extends UriFragmentNormalizer {
	private static final char PARAM_SEPARATOR = '&';
	private static final char PARAM_KEY_VALUE_SEPARATOR = '=';
	
	@Override
	public String normalize(String query) throws ValidationException {
		if (query == null) {
			return null;
		}
		
		StringBuilder queryBuilder = new StringBuilder(query.length() * 2);
		StringBuilder buffer = new StringBuilder(query.length());
		
		for (int i = 0; i < query.length(); i++) {
			final char c = query.charAt(i);
			if (c == PARAM_SEPARATOR) {
				queryBuilder.append(normalizeParam(buffer.toString()));
				queryBuilder.append(c);
				buffer = new StringBuilder(query.length());
			} else {
				buffer.append(c);
			}
		}
		
		queryBuilder.append(normalizeParam(buffer.toString()));
		
		return queryBuilder.toString();
	}

	private String normalizeParam(String param) throws ValidationException {
		final int separatorIndex = param.indexOf(PARAM_KEY_VALUE_SEPARATOR);
		
		if (separatorIndex == -1) {
			return super.normalize(param);
		}
		
		final String key = param.substring(0, separatorIndex);
		final String value = param.substring(separatorIndex + 1);
		
		final StringBuilder builder = new StringBuilder();
		builder.append(super.normalize(key));
		builder.append('=');
		builder.append(super.normalize(value));
		return builder.toString();
	}
}
