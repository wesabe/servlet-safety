package com.wesabe.servlet.normalizers.tests;

import static org.hamcrest.CoreMatchers.*;
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
			assertThat(normalizer.normalize(30), is(30));
			assertThat(normalizer.normalize(Integer.valueOf(30)), is(Integer.valueOf(30)));
		}
	}
	
	public static class Normalizing_An_Invalid_Port {
		private final PortNormalizer normalizer = new PortNormalizer();
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize(139000);
			} catch (ValidationException e) {
				assertThat((Integer) e.getValue(), is(139000));
				assertThat(e.getMessage(), is("Invalid value: 139000 (not a valid port number)"));
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
				assertThat((Integer) e.getValue(), is(-139000));
				assertThat(e.getMessage(), is("Invalid value: -139000 (not a valid port number)"));
			}
		}
	}
}
