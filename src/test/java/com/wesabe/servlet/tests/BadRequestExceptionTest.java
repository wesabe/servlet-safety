package com.wesabe.servlet.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.BadRequestException;

@RunWith(Enclosed.class)
public class BadRequestExceptionTest {
	public static class Throwing_A_Bad_Request_Exception {
		private Exception cause;
		private HttpServletRequest request;
		private BadRequestException exception;

		@Before
		public void setup() throws Exception {
			this.request = mock(HttpServletRequest.class);
			when(request.getMethod()).thenReturn("POST");
			when(request.getRequestURI()).thenReturn("/woohoo");
			when(request.getRemoteAddr()).thenReturn("1.2.3.4");
			
			this.cause = new Exception("on fire");
			this.exception = new BadRequestException(request, cause);
		}
		
		@Test
		public void itHasACause() throws Exception {
			assertThat(exception.getCause(), is((Throwable) cause));
		}
		
		@Test
		public void itHasAMessage() throws Exception {
			assertThat(exception.getMessage(), is("Bad request: POST to /woohoo from 1.2.3.4"));
		}
		
		@Test
		public void itHasABadRequest() throws Exception {
			assertThat(exception.getBadRequest(), is(sameInstance(request)));
		}
	}
}
