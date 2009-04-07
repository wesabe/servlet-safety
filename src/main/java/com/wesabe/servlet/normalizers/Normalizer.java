package com.wesabe.servlet.normalizers;

/**
 * A normalizer.
 * 
 * @author coda
 * 
 * @param <E>
 *            the type of values which the providers of this interface normalize
 */
public interface Normalizer<E> {
	/**
	 * Returns a normalized version of {@code input}.
	 * 
	 * @param input
	 *            an un-normalized value
	 * @return the normalized version of {@code input}
	 * @throws ValidationException
	 *             if {@code input} cannot be normalized
	 */
	public abstract E normalize(E input) throws ValidationException;
}
