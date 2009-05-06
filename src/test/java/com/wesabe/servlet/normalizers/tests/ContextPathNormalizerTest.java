package com.wesabe.servlet.normalizers.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.ContextPathNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class ContextPathNormalizerTest {
	public static class Normalizing_A_Valid_Context_Path {
		private final ContextPathNormalizer normalizer = new ContextPathNormalizer();
		
		@Test
		public void itReturnsThePath() throws Exception {
			assertThat(normalizer.normalize("path"), is("path"));
		}
	}
	
	public static class Normalizing_A_Malformed_Context_Path {
		private final ContextPathNormalizer normalizer = new ContextPathNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("path\0");
				fail("should have thrown a validation exception, but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: path\0 (not a valid context path)"));
			}
			
		}
	}
}
