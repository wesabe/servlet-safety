package com.wesabe.servlet.errors.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.mail.Message;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.wesabe.servlet.errors.DebugErrorReporter;
import com.wesabe.servlet.errors.ErrorReporter;

@RunWith(Enclosed.class)
public class DebugErrorReporterTest {
	public static class A_Debug_Reporter {
		private HttpServletRequest request;
		private HttpServletResponse response;
		private PrintWriter writer;
		private ErrorReporter reporter;
		private Message message;
		private ByteArrayOutputStream output;
		
		@Before
		public void setup() throws Exception {
			this.reporter = new DebugErrorReporter("from@example.com", "to@example.com", "service");
			
			mock(ServletOutputStream.class);
			this.output = new ByteArrayOutputStream();
			this.writer = new PrintWriter(output);
			this.request = mock(HttpServletRequest.class);
			this.response = mock(HttpServletResponse.class);
			when(response.getWriter()).thenReturn(writer);
			this.message = mock(Message.class);
		}
		
		@Test
		public void itHasANullSession() throws Exception {
			assertThat(reporter.getSession(), is(nullValue()));
		}
		
		@Test
		public void itAppendsTheDebugEmailToTheResponse() throws Exception {
			doAnswer(new Answer<Object>(){
				@Override
				public Object answer(InvocationOnMock invocation) throws Throwable {
					final OutputStream output = (OutputStream) invocation.getArguments()[0];
					output.write("THIS IS THE EMAIL".getBytes());
					return null;
				}
			}).when(message).writeTo(Mockito.any(OutputStream.class));
			
			reporter.deliver(message, request, response, new RuntimeException("WHAT"));
			
			writer.close();
			assertThat(output.toString(), is("========================\nAnd here's the email you would have received:\n\nTHIS IS THE EMAIL\n"));
		}
	}
}
