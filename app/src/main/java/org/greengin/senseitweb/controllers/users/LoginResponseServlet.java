package org.greengin.senseitweb.controllers.users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.greengin.senseitweb.logic.permissions.OpenIdManager;

public class LoginResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OpenIdManager.instance().updateLoginStatus(request);
		response.sendRedirect("status.jsp");
	}
}
