package com.wesabe.servlet.normalizers.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;

import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.HeaderNameNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class HeaderNameNormalizerTest {
	public static class Normalizing_Valid_Request_Headers {
		private final HeaderNameNormalizer normalizer = HeaderNameNormalizer.requestNormalizer();
		
		@Test
		public void itReturnsTheHeaderNameInLowercase() throws Exception {
			assertEquals("accept", normalizer.normalize("Accept"));
		}
	}
	
	public static class Normalizing_Invalid_Request_Headers {
		private final HeaderNameNormalizer normalizer = HeaderNameNormalizer.requestNormalizer();
		
		@Test
		public void itReturnsNull() throws Exception {
			assertNull(normalizer.normalize("Age"));
		}
	}
	
	public static class Normalizing_Malformed_Request_Headers {
		private final HeaderNameNormalizer normalizer = HeaderNameNormalizer.requestNormalizer();
		
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
	
	public static class Normalizing_Valid_Response_Headers {
		private final HeaderNameNormalizer normalizer = HeaderNameNormalizer.responseNormalizer();
		
		@Test
		public void itReturnsTheHeaderNameInLowercase() throws Exception {
			assertEquals("age", normalizer.normalize("Age"));
		}
	}
	
	public static class Normalizing_Invalid_Response_Headers {
		private final HeaderNameNormalizer normalizer = HeaderNameNormalizer.responseNormalizer();
		
		@Test
		public void itReturnsNull() throws Exception {
			assertNull(normalizer.normalize("X-Custom-Header"));
		}
	}
	
	public static class Normalizing_Malformed_Response_Headers {
		private final HeaderNameNormalizer normalizer = HeaderNameNormalizer.responseNormalizer();
		
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
