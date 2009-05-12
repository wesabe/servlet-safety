package com.wesabe.servlet.errors;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An {@link ErrorReporter} which pipes the error report to the local sendmail
 * binary.
 * 
 * @author coda
 */
public class SendmailErrorReporter extends ErrorReporter {
	private final String sendmailPath;
	
	/**
	 * Creates a new {@link SendmailErrorReporter}.
	 * 
	 * @param from the email address the error report will be from
	 * @param to the email address(es) the error report will be sent to
	 * @param serviceName the name of the monitored service
	 * @param sendmailPath the full path of the {@code sendmail} binary
	 * @throws AddressException if {@code from} or {@code to} are invalid email addresses
	 */
	public SendmailErrorReporter(String from, String to, String serviceName, String sendmailPath)
			throws AddressException {
		super(from, to, serviceName);
		
		final File sendmailFile = new File(sendmailPath);
		if (sendmailFile.exists() && sendmailFile.canExecute() && sendmailFile.isFile()) {
			try {
				this.sendmailPath = sendmailFile.getCanonicalPath();
			} catch (final IOException e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			throw new IllegalArgumentException(sendmailPath + " is not an executable");
		}
	}

	@Override
	public void deliver(Message message, HttpServletRequest request, HttpServletResponse response,
			Throwable e) throws IOException, MessagingException {
		// From SENDMAIL(1):
		//     -t     Extract recipients from message headers. These are added
		//            to any recipients specified on the command line.
		final Process sendmail = new ProcessBuilder(sendmailPath, "-t").start();
		final BufferedOutputStream output = new BufferedOutputStream(sendmail.getOutputStream());
		message.writeTo(output);
		output.close();
		try {
			sendmail.waitFor();
		} catch (final InterruptedException e1) {
			throw new IOException(e1);
		}
	}

	@Override
	public Session getSession() {
		return null;
	}

}
