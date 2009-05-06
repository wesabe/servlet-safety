package com.wesabe.servlet.normalizers.util.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.util.CaseInsensitiveSet;

@RunWith(Enclosed.class)
public class CaseInsensitiveSetTest {
	public static class A_Set_Of_Strings {
		private CaseInsensitiveSet strings = CaseInsensitiveSet.of(Locale.ENGLISH, "blah", "BLEE", "blOO");
		
		@Test
		public void itContainsLowercaseVersionsOfUppercaseStrings() throws Exception {
			assertThat(strings.contains("blee"), is(true));
		}
		
		@Test
		public void itContainsUppercaseVersionsOfUppercaseStrings() throws Exception {
			assertThat(strings.contains("BLEE"), is(true));
		}
		
		@Test
		public void itContainsMixedcaseVersionsOfUppercaseStrings() throws Exception {
			assertThat(strings.contains("BLee"), is(true));
		}
		
		@Test
		public void itDoesNotContainNonStrings() throws Exception {
			assertThat(strings.contains(200), is(false));
		}
		
		@Test
		public void itHasALocale() throws Exception {
			assertThat(strings.getLocale(), is(Locale.ENGLISH));
		}
		
		@Test
		public void itConvertsStringsToLowercase() throws Exception {
			assertThat(strings.lowercase("WOO"), is("woo"));
		}
		
		@Test
		public void itConvertsStringsToUppercase() throws Exception {
			assertThat(strings.uppercase("woo"), is("WOO"));
		}
	}
}
