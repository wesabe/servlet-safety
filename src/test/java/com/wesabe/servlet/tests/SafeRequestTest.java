package com.wesabe.servlet.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.BadRequestException;
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
	
	public static class Getting_The_Method extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itNormalizesTheMethodName() throws Exception {
			when(servletRequest.getMethod()).thenReturn("get");
			
			assertEquals("GET", request.getMethod());
			
			verify(servletRequest).getMethod();
		}
		
		@Test
		public void itThrowsABadRequestExceptionIfTheMethodIsInvalid() throws Exception {
			when(servletRequest.getMethod()).thenReturn("poop");
			
			try {
				request.getMethod();
				fail("should have thrown a BadRequestException, but didn't");
			} catch (BadRequestException e) {
				assertSame(servletRequest, e.getBadRequest());
			}
		}
	}
	
	public static class Getting_The_Scheme extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itNormalizesTheScheme() throws Exception {
			when(servletRequest.getScheme()).thenReturn("http");
			
			assertEquals("http", request.getScheme());
			
			verify(servletRequest).getScheme();
		}
		
		@Test
		public void itThrowsABadRequestExceptionIfTheSchemeIsInvalid() throws Exception {
			when(servletRequest.getScheme()).thenReturn("poop");
			
			try {
				request.getScheme();
				fail("should have thrown a BadRequestException, but didn't");
			} catch (BadRequestException e) {
				assertSame(servletRequest, e.getBadRequest());
			}
		}
	}
	
	public static class Getting_The_Server_Port extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itNormalizesTheServerPort() throws Exception {
			when(servletRequest.getServerPort()).thenReturn(80);
			
			assertEquals(80, request.getServerPort());
			
			verify(servletRequest).getServerPort();
		}
		
		@Test
		public void itThrowsABadRequestExceptionIfTheSchemeIsInvalid() throws Exception {
			when(servletRequest.getServerPort()).thenReturn(1112228888);
			
			try {
				request.getServerPort();
				fail("should have thrown a BadRequestException, but didn't");
			} catch (BadRequestException e) {
				assertSame(servletRequest, e.getBadRequest());
			}
		}
	}
	
	public static class Getting_A_Date_Header extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itReturnsAnyParsedDateHeader() throws Exception {
			when(servletRequest.getDateHeader("Last-Modified")).thenReturn(80L);
			
			assertEquals(80, request.getDateHeader("Last-Modified"));
			
			verify(servletRequest).getDateHeader("Last-Modified");
		}
		
		@Test
		public void itWrapsAFailedParseInABadRequestException() throws Exception {
			when(servletRequest.getDateHeader("Last-Modified")).thenThrow(new IllegalArgumentException("no"));
			
			try {
				request.getDateHeader("Last-Modified");
				fail("should have thrown a BadRequestException, but didn't");
			} catch (BadRequestException e) {
				assertSame(servletRequest, e.getBadRequest());
				assertTrue(e.getCause() instanceof IllegalArgumentException);
			}
		}
	}
	
	public static class Getting_An_Int_Header extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itReturnsAnyParsedIntHeader() throws Exception {
			when(servletRequest.getIntHeader("Age")).thenReturn(200);
			
			assertEquals(200, request.getIntHeader("Age"));
			
			verify(servletRequest).getIntHeader("Age");
		}
		
		@Test
		public void itWrapsAFailedParseInABadRequestException() throws Exception {
			when(servletRequest.getIntHeader("Age")).thenThrow(new IllegalArgumentException("no"));
			
			try {
				request.getIntHeader("Age");
				fail("should have thrown a BadRequestException, but didn't");
			} catch (BadRequestException e) {
				assertSame(servletRequest, e.getBadRequest());
				assertTrue(e.getCause() instanceof IllegalArgumentException);
			}
		}
	}
}
