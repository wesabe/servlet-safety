package com.wesabe.servlet.errors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableList;

/**
 * A base class for error reporting.
 * 
 * @author coda
 */
public abstract class ErrorReporter {
	private static final Logger LOGGER = Logger.getLogger(ErrorReporter.class.getCanonicalName());
	private final InternetAddress[] senders, recipients;
	private final String serviceName;
	
	/**
	 * Create a new error reporter.
	 * 
	 * @param from the email address from which the reports will be sent
	 * @param to the email address(es) to which the reports will be sent
	 * @param serviceName the name of the service being monitored for errors
	 * @throws AddressException if {@code from} or {@code to} are invalid email addresses
	 */
	public ErrorReporter(String from, String to, String serviceName) throws AddressException {
		this.senders = InternetAddress.parse(from);
		this.recipients = InternetAddress.parse(to);
		this.serviceName = serviceName;
	}
	
	/**
	 * Returns a list of the recipients of error reports.
	 * 
	 * @return a list of the recipients of error reports
	 */
	public List<InternetAddress> getRecipients() {
		return ImmutableList.of(recipients);
	}
	
	/**
	 * Returns a list of the senders of error reports.
	 * 
	 * @return a list of the senders of error reports
	 */
	public List<InternetAddress> getSenders() {
		return ImmutableList.of(senders);
	}
	
	/**
	 * Returns the service's name.
	 * 
	 * @return the service's name
	 */
	public String getServiceName() {
		return serviceName;
	}
	
	/**
	 * Returns the JavaMail session to be used to create email messages, or
	 * {@code null} if the reporter handles delivery on its own.
	 * 
	 * @return a JavaMail session or {@code null}
	 */
	public abstract Session getSession();
	
	/**
	 * Given an email message, a request, a response, and the exception thrown
	 * during the processing of the request, notify the reporter's recipients of
	 * the error.
	 */
	public abstract void deliver(Message message, HttpServletRequest request, HttpServletResponse response, Throwable e) throws IOException, MessagingException;
	
	/**
	 * Report an exception raised during the processing of a request.
	 */
	public void report(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
		try {
			deliver(buildEmail(request, exception), request, response, exception);
		} catch (final Exception e) {
			LOGGER.log(Level.SEVERE, "Unable to report error", e);
			LOGGER.log(Level.SEVERE, "Original error was:", exception);
		}
	}

	private Message buildEmail(HttpServletRequest request, Throwable e) throws MessagingException, IOException {
		final MimeMessage message = new MimeMessage(getSession());
		message.addFrom(senders);
		message.addRecipients(RecipientType.TO, recipients);
		message.setSentDate(new Date());
		message.setSubject(String.format("[ERROR] %s threw a %s", serviceName, e.getClass().getSimpleName()));
		message.setText(buildText(request, e), "UTF-8");
		return message;
	}

	private String buildText(HttpServletRequest request, Throwable e) throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final PrintWriter email = new PrintWriter(output);
		
		email.print("A ");
		email.print(e.getClass().getCanonicalName());
		email.print(" was thrown while responding to ");
		email.print(request.getRequestURL().toString());
		email.print(" on ");

		email.print(InetAddress.getLocalHost().getHostName());
		email.println(".\n");
		
		email.print("This request from ");
		email.print(request.getRemoteAddr());
		email.println(":\n");
		email.print("\t");
		email.println(request.toString().replaceAll("Authorization: .*", "Authorization: [REDACTED]").replace("\n", "\n\t"));
		
		email.println();
		email.println("Produced this error:\n");
		
		e.printStackTrace(email);
		email.flush();
		
		return output.toString();
	}
}
