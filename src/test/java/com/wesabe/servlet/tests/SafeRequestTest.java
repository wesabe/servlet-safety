package com.wesabe.servlet.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.SafeRequest;

@RunWith(Enclosed.class)
public class SafeRequestTest {
	private static abstract class Context {
		protected SafeRequest request;
		protected HttpServletRequest servletRequest;
		
		public void setup() throws Exception {
			this.servletRequest = mock(HttpServletRequest.class);
			this.request = new SafeRequest(servletRequest);
		}
	}
	
	
	public static class Getting_The_Authentication_Type extends Context {
		@Override
		@Before
		public void setup() throws Exception {
			super.setup();
			when(servletRequest.getAuthType()).thenReturn("Basic");
		}
		
		@Test
		public void itReturnsTheServletRequestsAuthType() throws Exception {
			assertEquals("Basic", request.getAuthType());
			
			verify(servletRequest).getAuthType();
		}
	}
}
