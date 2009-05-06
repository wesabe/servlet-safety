package com.wesabe.servlet.normalizers.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.HostnameNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class HostnameNormalizerTest {
	public static class Normalizing_A_Valid_Hostname {
		private HostnameNormalizer normalizer = new HostnameNormalizer();
		
		@Test
		public void itReturnsTheOriginalHostname() throws Exception {
			assertThat(normalizer.normalize("example.com"), is("example.com"));
		}
	}
	
	public static class Normalizing_A_Hostname_With_Invalid_Characters {
		private HostnameNormalizer normalizer = new HostnameNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("examp\nle.com");
				fail("should have thrown a validation exception, but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: examp\nle.com (not a valid hostname)"));
			}
		}
	}
	
	public static class Normalizing_A_Hostname_With_Invalid_Separators {
		private HostnameNormalizer normalizer = new HostnameNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("example..com");
				fail("should have thrown a validation exception, but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: example..com (not a valid hostname)"));
			}
		}
	}
}
