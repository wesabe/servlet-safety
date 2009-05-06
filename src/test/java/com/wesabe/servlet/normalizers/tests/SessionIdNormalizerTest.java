package com.wesabe.servlet.normalizers.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.SessionIdNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class SessionIdNormalizerTest {
	public static class Normalizing_Valid_Session_Ids {
		private final SessionIdNormalizer normalizer = new SessionIdNormalizer();
		
		@Test
		public void itReturnsTheSessionId() throws Exception {
			assertThat(normalizer.normalize("AHHDN9910HHDHA"), is("AHHDN9910HHDHA"));
		}
	}
	
	public static class Normalizing_Malformed_Session_Ids {
		private final SessionIdNormalizer normalizer = new SessionIdNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("AHAHAHA\0");
				fail("should have thrown a validation exception, but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: AHAHAHA\0 (not a valid session ID)"));
			}
		}
	}
	
	public static class Normalizing_Short_Session_Ids {
		private final SessionIdNormalizer normalizer = new SessionIdNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("WOO");
				fail("should have thrown a validation exception, but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: WOO (not a valid session ID)"));
			}
		}
	}
	
	public static class Normalizing_LONG_Session_Ids {
		private final SessionIdNormalizer normalizer = new SessionIdNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize("WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
				fail("should have thrown a validation exception, but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO (not a valid session ID)"));
			}
		}
	}
}
