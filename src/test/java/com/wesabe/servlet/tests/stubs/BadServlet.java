package com.wesabe.servlet.tests.stubs;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.addHeader("Date:\n\nGET /secrets HTTP/1.1", "MOO HOO HA HA");
		final PrintWriter writer = resp.getWriter();
		writer.println("Eels!");
		writer.close();
	}
}