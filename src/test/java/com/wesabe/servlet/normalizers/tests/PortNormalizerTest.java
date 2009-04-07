package com.wesabe.servlet.normalizers.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.PortNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class PortNormalizerTest {
	public static class Normalizing_A_Valid_Port {
		private final PortNormalizer normalizer = new PortNormalizer();
		
		@Test
		public void itReturnsThePort() throws Exception {
			assertEquals(30, normalizer.normalize(30));
			assertEquals(Integer.valueOf(30), normalizer.normalize(Integer.valueOf(30)));
		}
	}
	
	public static class Normalizing_An_Invalid_Port {
		private final PortNormalizer normalizer = new PortNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize(139000);
			} catch (ValidationException e) {
				assertEquals(139000, e.getValue());
				assertEquals("Invalid value: 139000 (not a valid port number)", e.getMessage());
			}
		}
	}
	
	public static class Normalizing_A_Negative_Port {
		private final PortNormalizer normalizer = new PortNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize(-139000);
			} catch (ValidationException e) {
				assertEquals(-139000, e.getValue());
				assertEquals("Invalid value: -139000 (not a valid port number)", e.getMessage());
			}
		}
	}
}
