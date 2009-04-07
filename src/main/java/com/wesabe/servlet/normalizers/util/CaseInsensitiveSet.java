package com.wesabe.servlet.normalizers.util;

import java.util.Locale;
import java.util.Set;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * A case-insensitive set of strings. Requires a {@link Locale} for
 * case-sensitivity rules.
 * 
 * @author coda
 *
 */
public class CaseInsensitiveSet extends ForwardingSet<String> {
	
	/**
	 * Returns a case-insensitive set of strings for a particular locale.
	 * 
	 * @param locale the locale for which case-sensitivity rules should be used
	 * @param elements the elements of the set in any particular order
	 * @return a set of case-insensitive strings
	 */
	public static CaseInsensitiveSet of(Locale locale, String... elements) {
		Builder<String> builder = ImmutableSet.builder();
		for (String string : elements) {
			builder.add(string.toLowerCase(locale));
		}
		return new CaseInsensitiveSet(builder.build(), locale);
	}
	
	private final ImmutableSet<String> strings;
	private final Locale locale;
	
	private CaseInsensitiveSet(ImmutableSet<String> strings, Locale locale) {
		this.strings = strings;
		this.locale = locale;
	}
	
	@Override
	public boolean contains(Object object) {
		if (object instanceof String) {
			return super.contains(((String) object).toLowerCase(locale));
		}
		return false;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	@Override
	protected Set<String> delegate() {
		return strings;
	}
	
}
