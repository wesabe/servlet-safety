package com.wesabe.servlet.normalizers.util.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.wesabe.servlet.normalizers.util.FilteredEnumeration;

@RunWith(Enclosed.class)
public class FilteredEnumerationTest {
	public static class Filtering_An_Enumeration_Of_Strings {
		private final Predicate<String> predicate = new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return input.contains("e");
			}
		};
		private Enumeration<String> strings;
		private Enumeration<String> filteredStrings;
		
		@Before
		public void setup() throws Exception {
			this.strings = Collections.enumeration(Lists.newArrayList("one", "two", "three"));
			this.filteredStrings = new FilteredEnumeration<String>(strings, predicate);
		}
		
		@Test
		public void itEnumeratesAllMatchingStrings() throws Exception {
			final List<String> enumeratedStrings = Lists.newArrayList();
			while (filteredStrings.hasMoreElements()) {
				enumeratedStrings.add(filteredStrings.nextElement());
			}
			assertThat(enumeratedStrings, hasItems("one", "three"));
			assertThat(enumeratedStrings, not(hasItem("two")));
		}
		
		@Test
		public void itThrowsANoSuchElementExceptionWhenOutOfElements() throws Exception {
			while (filteredStrings.hasMoreElements()) {
				filteredStrings.nextElement();
			}
			
			try {
				filteredStrings.nextElement();
				fail("should have thrown a NoSuchElementException, but didn't");
			} catch (NoSuchElementException e) {
				assertTrue(true);
			}
		}
		
	}
}
