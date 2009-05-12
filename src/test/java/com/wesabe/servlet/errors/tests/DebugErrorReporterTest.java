package com.wesabe.servlet.errors.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.errors.DebugErrorReporter;
import com.wesabe.servlet.errors.ErrorReporter;

@RunWith(Enclosed.class)
public class DebugErrorReporterTest {
	private static class MockMessage extends MimeMessage {
		public MockMessage() {
			super((Session) null);
		}
		
		@Override
		protected void updateMessageID() throws MessagingException {
			setHeader("Message-ID", "<YAY>");
		}
	}
	
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
			
			this.output = new ByteArrayOutputStream();
			this.writer = new PrintWriter(output);
			this.request = mock(HttpServletRequest.class);
			this.response = mock(HttpServletResponse.class);
			when(response.getWriter()).thenReturn(writer);
			this.message = new MockMessage();
			message.setSubject("[ERROR] Something bad happened.");
			message.setText("Make it stop.");
		}
		
		@Test
		public void itHasANullSession() throws Exception {
			assertThat(reporter.getSession(), is(nullValue()));
		}
		
		@Test
		public void itAppendsTheDebugEmailToTheResponse() throws Exception {
			reporter.deliver(message, request, response, new RuntimeException("WHAT"));
			writer.close();
			
			assertThat(
				output.toString(),
				is(
					"========================\n" +
					"And here's the email you would have received:\n" +
					"\n" +
					"Message-ID: <YAY>\r\n" +
					"Subject: [ERROR] Something bad happened.\r\n" +
					"MIME-Version: 1.0\r\n" +
					"Content-Type: text/plain; charset=us-ascii\r\n" +
					"Content-Transfer-Encoding: 7bit\r\n" +
					"\r\n" +
					"Make it stop.\n"
				)
			);
		}
	}
}
