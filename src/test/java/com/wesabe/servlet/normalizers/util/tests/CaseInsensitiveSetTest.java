package com.wesabe.servlet.normalizers.util.tests;

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
			assertTrue(strings.contains("blee"));
		}
		
		@Test
		public void itContainsUppercaseVersionsOfUppercaseStrings() throws Exception {
			assertTrue(strings.contains("BLEE"));
		}
		
		@Test
		public void itContainsMixedcaseVersionsOfUppercaseStrings() throws Exception {
			assertTrue(strings.contains("BLee"));
		}
		
		@Test
		public void itDoesNotContainNonStrings() throws Exception {
			assertFalse(strings.contains(200));
		}
		
		@Test
		public void itHasALocale() throws Exception {
			assertEquals(Locale.ENGLISH, strings.getLocale());
		}
		
		@Test
		public void itConvertsStringsToLowercase() throws Exception {
			assertEquals("woo", strings.lowercase("WOO"));
		}
		
		@Test
		public void itConvertsStringsToUppercase() throws Exception {
			assertEquals("WOO", strings.uppercase("woo"));
		}
	}
}
