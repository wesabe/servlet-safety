package com.wesabe.servlet.normalizers.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.ParameterNameNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class ParameterNameNormalizerTest {
	public static class Normalizing_Valid_Param_Names {
		private final ParameterNameNormalizer normalizer = new ParameterNameNormalizer();
		
		@Test
		public void itReturnsTheParamName() throws Exception {
			assertThat(normalizer.normalize("dingo"), is("dingo"));
		}
	}
	
	public static class Normalizing_Malformed_Param_Names {
		private final ParameterNameNormalizer normalizer = new ParameterNameNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("dingo\0");
				fail("should have thrown a validation exception, but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: dingo\0 (not a valid parameter name)"));
			}
		}
	}
}
