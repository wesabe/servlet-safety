package com.wesabe.servlet.errors.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.List;

import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.wesabe.servlet.errors.SendmailErrorReporter;

@RunWith(Enclosed.class)
public class SendmailErrorReporterTest {
	private static String read(String filename) throws Exception {
		final StringBuilder builder = new StringBuilder();
		final BufferedReader reader = new BufferedReader(new FileReader(filename));
		final char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			builder.append(String.valueOf(buf, 0, numRead));
		}
		reader.close();
		return builder.toString();
	}
	
	private static List<String> normalize(String s) {
		final List<String> lines = ImmutableList.of(s.split("(\\r\\n|\\r|\\n)"));
		return ImmutableList.copyOf(Iterables.transform(lines, new Function<String, String>(){
			@Override
			public String apply(String from) {
				return from.trim();
			}
		}));
	}
	
	public static class A_Sendmail_Error_Reporter {
		@Test
		public void itDoesNotAcceptANonexistentSendmailBinary() throws Exception {
			try {
				new SendmailErrorReporter("from@example.com", "to@example.com", "thinky", "src/test/woo.sh");
				fail("should have thrown an IllegalArgumentException but didn't");
			} catch (final IllegalArgumentException e) {
				assertThat(e.getMessage(), is("src/test/woo.sh is not an executable"));
			}
		}
		
		@Test
		public void itDoesNotAcceptADirectoryAsSendmailBinary() throws Exception {
			try {
				new SendmailErrorReporter("from@example.com", "to@example.com", "thinky", "src/test/java/");
				fail("should have thrown an IllegalArgumentException but didn't");
			} catch (final IllegalArgumentException e) {
				assertThat(e.getMessage(), is("src/test/java/ is not an executable"));
			}
		}
		
		@Test
		public void itDoesNotAcceptANonexecutableAsSendmailBinary() throws Exception {
			try {
				new SendmailErrorReporter("from@example.com", "to@example.com", "thinky", "src/test/java/com/wesabe/servlet/errors/tests/SendmailErrorReporterTest.java");
				fail("should have thrown an IllegalArgumentException but didn't");
			} catch (final IllegalArgumentException e) {
				assertThat(e.getMessage(), is("src/test/java/com/wesabe/servlet/errors/tests/SendmailErrorReporterTest.java is not an executable"));
			}
		}
		
		@Test
		public void itHasNullSessions() throws Exception {
			final SendmailErrorReporter reporter = new SendmailErrorReporter("from@example.com", "to@example.com", "thinky", "src/test/resources/fakesendmail.sh");
			assertThat(reporter.getSession(), is(nullValue()));
		}
	}
	
	public static class Reporting_An_Error {
		private MimeMessage message;
		private HttpServletRequest request;
		private HttpServletResponse response;
		private Throwable exception;
		private SendmailErrorReporter reporter;
		
		@Before
		public void setup() throws Exception {
			this.request = mock(HttpServletRequest.class);
			this.response = mock(HttpServletResponse.class);
			this.exception = mock(Throwable.class);
			
			this.reporter = new SendmailErrorReporter("from@example.com", "to@example.com", "thinky", "src/test/resources/fakesendmail.sh");
			
			this.message = new MimeMessage((Session) null);
			message.addFrom(InternetAddress.parse("from@example.com"));
			message.addRecipients(RecipientType.TO, InternetAddress.parse("to@example.com"));
			message.setSubject("RAWESOME");
			message.setSentDate(new Date());
			message.setText("I AM CONFUSED", "UTF-8");
		}
		
		@After
		public void cleanup() {
			final File file = new File("sent-mail.txt");
			file.delete();
		}
		
		@Test
		public void itPipesTheMessageToSendmail() throws Exception {
			reporter.deliver(message, request, response, exception);
			
			final List<String> msgLines = normalize(read("sent-mail.txt"));
			
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			message.writeTo(output);
			final List<String> outputLines = normalize(output.toString());
			
			assertThat(msgLines.size(), is(outputLines.size()));
			assertThat(msgLines.get(1), is(outputLines.get(1)));
			assertThat(msgLines.get(2), is(outputLines.get(2)));
			assertThat(msgLines.get(4), is(outputLines.get(4)));
			assertThat(msgLines.get(9), is(outputLines.get(9)));
		}
	}
}
