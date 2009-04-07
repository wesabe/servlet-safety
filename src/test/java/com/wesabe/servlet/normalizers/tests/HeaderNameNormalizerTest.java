package com.wesabe.servlet.normalizers.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;

import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.HeaderNameNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class HeaderNameNormalizerTest {
	public static class Normalizing_Valid_Header_Names {
		private final HeaderNameNormalizer normalizer = new HeaderNameNormalizer();
		
		@Test
		public void itReturnsTheHeaderName() throws Exception {
			assertEquals("Accept", normalizer.normalize("Accept"));
		}
	}
	
	public static class Normalizing_Malformed_Request_Headers {
		private final HeaderNameNormalizer normalizer = new HeaderNameNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("Age\0");
				fail("should have thrown a validation exception, but didn't");
			} catch (ValidationException e) {
				assertEquals("Invalid value: Age\0 (not a valid HTTP header)", e.getMessage());
			}
			
		}
	}
}
