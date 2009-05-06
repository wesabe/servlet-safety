package com.wesabe.servlet.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

import com.wesabe.servlet.SafeFilter;
import com.wesabe.servlet.tests.stubs.BadServlet;
import com.wesabe.servlet.tests.stubs.GoodServlet;

@RunWith(Enclosed.class)
public class SafeFilterTest {
	public static class Filtering_A_Valid_Request_And_Response {
		private ServletTester tester;
		
		@Before
		public void setup() throws Exception {
			Logger.getLogger("org.mortbay").setLevel(Level.OFF);
			Logger.getLogger("com.wesabe").setLevel(Level.OFF);
			
			this.tester = new ServletTester();
			tester.addFilter(SafeFilter.class, "/*", 0);
			tester.addServlet(GoodServlet.class, "/*");
			tester.start();
		}
		
		@After
		public void teardown() throws Exception {
			tester.stop();
		}
		
		@Test
		public void itDoesNotModifyTheRequestOrResponse() throws Exception {
			final HttpTester request = new HttpTester();
			final HttpTester response = new HttpTester();
			
			request.setMethod("GET");
			request.setHeader("Host","tester");
			request.setURI("/hello?world");
			request.setVersion("HTTP/1.1");
			
			response.parse(tester.getResponses(request.generate()));
			
			assertThat(response.getStatus(), is(200));
			assertThat(response.getContent(), is("Hello, world\n"));
		}
	}
	
	public static class Filtering_An_Invalid_Request {
		private ServletTester tester;
		
		@Before
		public void setup() throws Exception {
			Logger.getLogger("org.mortbay").setLevel(Level.OFF);
			Logger.getLogger("com.wesabe").setLevel(Level.OFF);
			
			this.tester = new ServletTester();
			tester.addFilter(SafeFilter.class, "/*", 0);
			tester.addServlet(GoodServlet.class, "/*");
			tester.start();
		}
		
		@After
		public void teardown() throws Exception {
			tester.stop();
		}
		
		@Test
		public void itDoesNotModifyTheRequestOrResponse() throws Exception {
			final HttpTester request = new HttpTester();
			final HttpTester response = new HttpTester();
			
			request.setMethod("GET");
			request.setHeader("Host","tester");
			request.setURI("/hello?either%25252for");
			request.setVersion("HTTP/1.1");
			
			response.parse(tester.getResponses(request.generate()));
			
			assertThat(response.getStatus(), is(400));
			assertThat(
				response.getContent(),
				is("The request could not be understood by the server due to malformed syntax. Do not re-send this request without modifications.\n")
			);
		}
	}
	
	public static class Filtering_An_Invalid_Response {
		private ServletTester tester;
		
		@Before
		public void setup() throws Exception {
			Logger.getLogger("org.mortbay").setLevel(Level.OFF);
			Logger.getLogger("com.wesabe").setLevel(Level.OFF);
			
			this.tester = new ServletTester();
			tester.addFilter(SafeFilter.class, "/*", 0);
			tester.addServlet(BadServlet.class, "/*");
			tester.start();
		}
		
		@After
		public void teardown() throws Exception {
			tester.stop();
		}
		
		@Test
		public void itDoesNotModifyTheRequestOrResponse() throws Exception {
			final HttpTester request = new HttpTester();
			final HttpTester response = new HttpTester();
			
			request.setMethod("GET");
			request.setHeader("Host","tester");
			request.setURI("/hello?world");
			request.setVersion("HTTP/1.1");
			
			response.parse(tester.getResponses(request.generate()));
			
			assertThat(response.getStatus(), is(500));
			assertThat(
				response.getContent(),
				is("The server encountered an unexpected condition which prevented it from fulfilling the request.\n")
			);
		}
	}
}
