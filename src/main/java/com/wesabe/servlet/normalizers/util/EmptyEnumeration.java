package com.wesabe.servlet.normalizers.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * An empty enumeration.
 * 
 * @author coda
 *
 * @param <E> the type of elements this enumeration will never have
 */
public class EmptyEnumeration<E> implements Enumeration<E> {
	private static final EmptyEnumeration<Object> EMPTY_ENUMERATION = new EmptyEnumeration<Object>();
	
	/**
	 * Build an empty {@link Enumeration} of a particular type.
	 * 
	 * @param <E> the type of elements the enumeration won't have
	 * @return an empty enumeration
	 */
	@SuppressWarnings("unchecked")
	public static <E> EmptyEnumeration<E> build() {
		return (EmptyEnumeration<E>) EMPTY_ENUMERATION;
	}
	
	private EmptyEnumeration() {
		// hurr durr durr hur
	}
	
	@Override
	public boolean hasMoreElements() {
		return false;
	}

	@Override
	public E nextElement() {
		throw new NoSuchElementException();
	}
}
