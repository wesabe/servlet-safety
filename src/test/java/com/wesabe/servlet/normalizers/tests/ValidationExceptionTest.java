package com.wesabe.servlet.normalizers.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class ValidationExceptionTest {
	public static class Throwing_A_Validation_Exception_Directly {
		@Test
		public void itHasAValue() throws Exception {
			ValidationException exception = new ValidationException("200", "is even");
			assertThat((String) exception.getValue(), is("200"));
		}
		
		@Test
		public void itHasADescriptiveMessage() throws Exception {
			ValidationException exception = new ValidationException("200", "is even");
			assertThat(exception.getMessage(), is("Invalid value: 200 (is even)"));
		}
	}
	
	public static class Throwing_A_Validation_Exception_With_An_Underlying_Cause {
		@Test
		public void itHasADescriptiveMessage() throws Exception {
			Exception cause = new Exception("eff");
			ValidationException exception = new ValidationException("200", cause);
			assertThat(exception.getMessage(), is("Invalid value: 200 (eff)"));
		}
		
		@Test
		public void itHasACause() throws Exception {
			Exception cause = new Exception("eff");
			ValidationException exception = new ValidationException("200", cause);
			assertThat(exception.getCause(), is((Throwable) cause));
		}
	}
}
