package com.wesabe.servlet.errors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An {@link ErrorReporter} implementation which simply appends the error report
 * to the response.
 * <p>
 * <strong>DO NOT USE THIS CLASS IN PRODUCTION.</strong>
 * 
 * @author coda
 */
public class DebugErrorReporter extends ErrorReporter {

	public DebugErrorReporter(String from, String to, String serviceName) throws AddressException {
		super(from, to, serviceName);
	}

	@Override
	public Session getSession() {
		return null;
	}

	@Override
	public void deliver(Message message, HttpServletRequest request, HttpServletResponse response, Throwable e) throws IOException, MessagingException {
		final PrintWriter writer = response.getWriter();
		writer.println("========================");
		writer.println("And here's the email you would have received:\n");
		
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		message.writeTo(output);
		output.flush();
		writer.println(output.toString());
	}

}
