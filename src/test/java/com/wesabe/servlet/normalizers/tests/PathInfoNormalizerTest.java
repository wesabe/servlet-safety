package com.wesabe.servlet.normalizers.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.PathInfoNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class PathInfoNormalizerTest {
	public static class Normalizing_A_Valid_Path {
		private PathInfoNormalizer normalizer = new PathInfoNormalizer();
		
		@Test
		public void itReturnsTheOriginalPath() throws Exception {
			assertEquals("blee", normalizer.normalize("blee"));
		}
	}
	
	public static class Normalizing_A_Path_With_Invalid_Characters {
		private PathInfoNormalizer normalizer = new PathInfoNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("bloo\0");
				fail("should have thrown a validation exception, but didn't");
			} catch (ValidationException e) {
				assertEquals("Invalid value: bloo\0 (not a valid path)", e.getMessage());
			}
		}
	}
}
