package com.wesabe.servlet.normalizers;

import java.util.Locale;

import com.wesabe.servlet.normalizers.util.CaseInsensitiveSet;
import com.wesabe.servlet.normalizers.util.CharacterSet;

/**
 * Normalizes HTTP header field names.
 * 
 * @author coda
 */
public class HeaderNameNormalizer implements Normalizer<String> {
	private static final CharacterSet VALID_CHARACTERS = CharacterSet.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-");
	private final CaseInsensitiveSet headers;
	
	/**
	 * Returns a {@link HeaderNameNormalizer} suitable for normalizing HTTP
	 * header names on an incoming request.
	 */
	public static HeaderNameNormalizer requestNormalizer() {
		return new HeaderNameNormalizer(
			CaseInsensitiveSet.of(
				Locale.US,
				// general, entity, and request headers
				"Cache-Control", "Connection", "Date", "Pragma", "Trailer",
				"Transfer-Encoding", "Upgrade", "Via", "Warning", "Allow",
				"Content-Encoding", "Content-Language", "Content-Length",
				"Content-Location", "Content-MD5", "Content-Range",
				"Content-Type", "Expires", "Last-Modified", "Accept",
				"Accept-Charset", "Accept-Encoding", "Accept-Language",
				"Authorization", "Expect", "From", "Host", "If-Match",
				"If-Modified-Since", "If-None-Match", "If-Range",
				"If-Unmodified-Since", "Max-Forwards", "Proxy-Authorization",
				"Range", "Referer", "TE", "User-Agent"
			)
		);
	}
	
	/**
	 * Returns a {@link HeaderNameNormalizer} suitable for normalizing HTTP
	 * header names on an outgoing response.
	 */
	public static HeaderNameNormalizer responseNormalizer() {
		return new HeaderNameNormalizer(
			CaseInsensitiveSet.of(
				Locale.US,
				// general, entity, and response headers
				"Cache-Control", "Connection", "Date", "Pragma", "Trailer",
				"Transfer-Encoding", "Upgrade", "Via", "Warning", "Allow",
				"Content-Encoding", "Content-Language", "Content-Length",
				"Content-Location", "Content-MD5", "Content-Range",
				"Content-Type", "Expires", "Last-Modified", "Accept-Ranges",
				"Age", "ETag", "Location", "Proxy-Authenticate", "Retry-After",
				"Server", "Vary", "WWW-Authenticate"
			)
		);
	}
	
	private HeaderNameNormalizer(CaseInsensitiveSet headers) {
		this.headers = headers;
	}
	
	/**
	 * Normalizing a header name will do one of three things.
	 * <p>
	 * If the header name is well-formed and is a valid header name, its
	 * lowercase form is returned.
	 * <p>
	 * If the header name is well-formed and is not a valid header name (e.g.,
	 * {@code X-Custom-Header}), {@code null} is returned. The header should be
	 * dropped.
	 * <p>
	 * If the header name is malformed (e.g., contains spurious characters or
	 * is too long), a {@link ValidationException} is thrown and the request
	 * should be rejected.
	 */
	@Override
	public String normalize(String name) throws ValidationException {
		if (VALID_CHARACTERS.composes(name)) {
			if (headers.contains(name)) {
				return headers.lowercase(name);
			}
			
			return null;
		}
		
		throw new ValidationException(name, "not a valid HTTP header");
	}
}
