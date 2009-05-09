package com.wesabe.servlet.errors.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.wesabe.servlet.errors.ErrorReporter;

@RunWith(Enclosed.class)
public class ErrorReporterTest {
	private static class FakeErrorReporter extends ErrorReporter {
		private final List<Message> messages = Lists.newLinkedList();
		
		public FakeErrorReporter(String from, String to, String serviceName)
				throws AddressException {
			super(from, to, serviceName);
		}

		@Override
		public void deliver(Message message, HttpServletRequest request,
				HttpServletResponse response, Throwable e) throws IOException, MessagingException {
			messages.add(message);
		}

		@Override
		public Session getSession() {
			return null;
		}
		
		public List<Message> getMessages() {
			return messages;
		}
	}
	
	public static class An_Error_Reporter {
		private ErrorReporter reporter;
		
		@Before
		public void setup() throws Exception {
			this.reporter = new FakeErrorReporter("from@example.com", "to@example.com", "system");
		}
		
		@Test
		public void itHasASender() throws Exception {
			assertThat(
				reporter.getSenders(),
				is((List<InternetAddress>) ImmutableList.of(InternetAddress.parse("from@example.com")))
			);
		}
		
		@Test
		public void itHasARecipient() throws Exception {
			assertThat(
				reporter.getRecipients(),
				is((List<InternetAddress>) ImmutableList.of(InternetAddress.parse("to@example.com")))
			);
		}
		
		@Test
		public void itHasAServiceName() throws Exception {
			assertThat(reporter.getServiceName(), is("system"));
		}
	}
	
	public static class The_Email_Sent_When_An_Exception_Is_Thrown {
		private HttpServletRequest request;
		private HttpServletResponse response;
		private FakeErrorReporter reporter;
		private String email;
		private Throwable exception;
		private MimeMessage message;
		
		@Before
		public void setup() throws Exception {
			this.request = mock(HttpServletRequest.class);
			when(request.getRequestURL()).thenReturn(new StringBuffer("http://example.com/thing"));
			when(request.toString()).thenReturn(
				"GET /thing HTTP/1.1\n" +
				"User-Agent: A Unit Test\n" +
				"Authorization: Basic SNUPERSNEAKY"
			);
			when(request.getRemoteAddr()).thenReturn("123.89.11.19");
			
			this.response = mock(HttpServletResponse.class);
			
			this.reporter = new FakeErrorReporter("exceptions@example.com", "developers@example.com", "unittest");
			
			try {
				throw new RuntimeException("THE BEES THEY'RE IN MY EYES");
			} catch (final RuntimeException exception) {
				this.exception = exception;
				reporter.report(request, response, exception);
				this.message = (MimeMessage) reporter.getMessages().get(0);
				final ByteArrayOutputStream output = new ByteArrayOutputStream();
				message.writeTo(output);
				
				this.email = output.toString();
			}
		}
		
		@Test
		public void itIsFromTheSpecifiedAddress() throws Exception {
			assertThat(email, containsString("From: exceptions@example.com"));
		}
		
		@Test
		public void itIsToTheSpecifiedRecipient() throws Exception {
			assertThat(email, containsString("To: developers@example.com"));
		}
		
		@Test
		public void itHasADescriptiveSubject() throws Exception {
			assertThat(email, containsString("Subject: [ERROR] unittest threw a RuntimeException"));
		}
		
		@Test
		public void itHasTheClientsIpAddress() throws Exception {
			assertThat(email, containsString("123.89.11.19"));
		}
		
		@Test
		public void itHasTheRequest() throws Exception {
			assertThat(email, containsString("GET /thing HTTP/1.1"));
			assertThat(email, containsString("User-Agent: A Unit Test"));
		}
		
		@Test
		public void itRedactsAuthorizationCredentials() throws Exception {
			assertThat(email, containsString("Authorization: [REDACTED]"));
		}
		
		@Test
		public void itSendsPlainUTF8() throws Exception {
			assertThat(email, containsString("Content-Type: text/plain; charset=UTF-8"));
		}
		
		@Test
		public void itHasTheExceptionStackTrace() throws Exception {
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			final PrintWriter writer = new PrintWriter(output);
			exception.printStackTrace(writer);
			
			assertThat(email, containsString(output.toString()));
		}
	}
}
