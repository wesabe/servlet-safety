package com.wesabe.servlet.normalizers.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import com.google.common.base.Predicate;

/**
 * An {@link Enumeration} wrapper which only returns elements from an existing
 * {@link Enumeration} which satisfy a {@link Predicate}.
 * <p>
 * <b>Not thread safe.</b>
 * 
 * @author coda
 *
 * @param <E> the element type of the enumeration
 */
public class FilteredEnumeration<E> implements Enumeration<E> {
	private final Enumeration<E> enumeration;
	private final Predicate<E> predicate;
	private E nextElement;
	private boolean done;
	
	public FilteredEnumeration(Enumeration<E> enumeration, Predicate<E> predicate) {
		this.enumeration = enumeration;
		this.predicate = predicate;
		this.nextElement = null;
		this.done = false;
		advance();
	}
	
	@Override
	public boolean hasMoreElements() {
		return !done;
	}

	@Override
	public E nextElement() {
		try {
			if (done) {
				throw new NoSuchElementException();
			}
			return nextElement;
		} finally {
			advance();
		}
	}
	
	private void advance() {
		while (enumeration.hasMoreElements()) {
			this.nextElement = enumeration.nextElement();
			if (predicate.apply(nextElement)) {
				return;
			}
		}
		
		this.done = true;
	}
}
