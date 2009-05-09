package com.wesabe.servlet.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.wesabe.servlet.ErrorReporterFilter;
import com.wesabe.servlet.errors.ErrorReporter;

@RunWith(Enclosed.class)
public class ErrorReporterFilterTest {
	public static class An_Error_Reporter_Filter {
		@Test
		public void itDoesntDoAnythingOnInit() throws Exception {
			try {
				new ErrorReporterFilter(null, null).init(null);
			} catch (final Throwable e) {
				fail("shouldn't have thrown a " + e.getClass().getCanonicalName() + " but it did");
			}
		}
		
		@Test
		public void itDoesntDoAnythingOnDestroy() throws Exception {
			try {
				new ErrorReporterFilter(null, null).destroy();
			} catch (final Throwable e) {
				fail("shouldn't have thrown a " + e.getClass().getCanonicalName() + " but it did");
			}
		}
	}
	
	public static class Filtering_A_Successful_Request {
		private HttpServletRequest request;
		private HttpServletResponse response;
		private FilterChain chain;
		private ErrorReporter reporter;
		private ErrorReporterFilter filter;
		
		@Before
		public void setup() throws Exception {
			this.request = mock(HttpServletRequest.class);
			this.response = mock(HttpServletResponse.class);
			this.chain = mock(FilterChain.class);
			this.reporter = mock(ErrorReporter.class);
			this.filter = new ErrorReporterFilter(reporter, "NO YUO");
		}
		
		@Test
		public void itPassesTheRequestAndResponseToTheChain() throws Exception {
			filter.doFilter(request, response, chain);
			
			verify(chain).doFilter(request, response);
		}
		
		@Test
		public void itDoesntReportAnyErrors() throws Exception {
			filter.doFilter(request, response, chain);
			
			verify(reporter, never()).report(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class), Mockito.any(Throwable.class));
		}
	}
	
	public static class Filtering_An_Unsuccessful_Request {
		private HttpServletRequest request;
		private HttpServletResponse response;
		private FilterChain chain;
		private ErrorReporter reporter;
		private ErrorReporterFilter filter;
		private ByteArrayOutputStream output;
		private Throwable exception;
		
		@Before
		public void setup() throws Exception {
			this.request = mock(HttpServletRequest.class);
			this.response = mock(HttpServletResponse.class);
			this.chain = mock(FilterChain.class);
			this.reporter = mock(ErrorReporter.class);
			this.filter = new ErrorReporterFilter(reporter, "NO YUO");
			
			this.output = new ByteArrayOutputStream();
			when(response.getWriter()).thenReturn(new PrintWriter(output));
			
			this.exception = new NullPointerException("AUGH");
			doThrow(exception).when(chain).doFilter(request, response);
		}
		
		@Test
		public void itReturnsABlandErrorMessageToTheClientAndReportsTheError() throws Exception {
			filter.doFilter(request, response, chain);
			
			
			final InOrder inOrder = inOrder(chain, reporter, response);
			inOrder.verify(chain).doFilter(request, response);
			inOrder.verify(response).reset();
			inOrder.verify(response).setStatus(500);
			inOrder.verify(reporter).report(request, response, exception);
			
			assertThat(output.toString(), is("The server encountered an unexpected condition which prevented it from fulfilling the request.\n\nNO YUO\n"));
		}
	}
}
