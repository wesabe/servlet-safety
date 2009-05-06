package com.wesabe.servlet.normalizers.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;

import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.MethodNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class MethodNormalizerTest {
	public static class Normalizing_A_Valid_Uppercase_Method {
		private final MethodNormalizer normalizer = new MethodNormalizer();
		
		@Test
		public void itReturnsTheOriginalString() throws Exception {
			assertThat(normalizer.normalize("GET"), is("GET"));
		}
	}
	
	public static class Normalizing_A_Valid_Lowercase_Method {
		private final MethodNormalizer normalizer = new MethodNormalizer();
		
		@Test
		public void itReturnsTheMethodInUppcase() throws Exception {
			assertThat(normalizer.normalize("get"), is("GET"));
		}
	}
	
	public static class Normalizing_An_Invalid_Method {
		private final MethodNormalizer normalizer = new MethodNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("DINGO\nDINGO");
				fail("should have thrown a ValidationException but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: DINGO\nDINGO (not a member of [get, put, post, delete, head, options])"));
			}
		}
	}
}
