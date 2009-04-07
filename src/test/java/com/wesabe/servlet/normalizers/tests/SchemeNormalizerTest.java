package com.wesabe.servlet.normalizers.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.SchemeNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class SchemeNormalizerTest {
	public static class Normalizing_A_Lowercase_Scheme {
		private final SchemeNormalizer normalizer = new SchemeNormalizer();
		
		@Test
		public void itReturnsTheSchemeAsLowercase() throws Exception {
			assertEquals("http", normalizer.normalize("http"));
		}
	}
	
	public static class Normalizing_An_Uppercase_Scheme {
		private final SchemeNormalizer normalizer = new SchemeNormalizer();
		
		@Test
		public void itReturnsTheSchemeAsLowercase() throws Exception {
			assertEquals("https", normalizer.normalize("HTTPS"));
		}
	}
	
	public static class Normalizing_An_Invalid_Scheme {
		private final SchemeNormalizer normalizer = new SchemeNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("dingo");
			} catch (ValidationException e) {
				assertEquals("dingo", e.getValue());
				assertEquals("Invalid value: dingo (not a member of [http, https])", e.getMessage());
			}
		}
	}
}
