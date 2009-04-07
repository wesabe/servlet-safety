package com.wesabe.servlet.normalizers;

/**
 * Normalizes TCP or UDP port numbers (e.g., {@code >= 0 && <= 65535}).
 * 
 * @author coda
 */
public class PortNormalizer implements Normalizer<Integer> {
	private static final int MIN_VALID_PORT = 0;
	private static final int MAX_VALID_PORT = 0xFFFF;

	@Override
	public Integer normalize(Integer port) throws ValidationException {
		return Integer.valueOf(normalize(port.intValue()));
	}
	
	/**
	 * A primitive version of {@link #normalize(Integer)}.
	 */
	public int normalize(int port) throws ValidationException {
		if (port >= MIN_VALID_PORT && port <= MAX_VALID_PORT) {
			return port;
		}
		
		throw new ValidationException(port, "not a valid port number");
	}

}
