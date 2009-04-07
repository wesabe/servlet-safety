package com.wesabe.servlet.normalizers.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.HeaderValueNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class HeaderValueNormalizerTest {
	public static class Normalizing_Valid_Header_Values {
		private final HeaderValueNormalizer normalizer = new HeaderValueNormalizer();
		
		@Test
		public void itPassesTheValueThrough() throws Exception {
			assertEquals("woo", normalizer.normalize("woo"));
		}
	}
	
	public static class Normalizing_Invalid_Header_Values {
		private final HeaderValueNormalizer normalizer = new HeaderValueNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("wo\0o");
				fail("should have thrown a ValidationException but didn't");
			} catch (ValidationException e) {
				assertEquals("Invalid value: wo\0o (not a valid HTTP header value)", e.getMessage());
			}
		}
	}
}
