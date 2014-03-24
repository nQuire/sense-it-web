package org.greengin.senseitweb.controllers.users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.greengin.senseitweb.logic.permissions.OpenIdManager;
import org.greengin.senseitweb.logic.permissions.OpenIdManager.Provider;

public class LoginRedirectServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Provider provider = Provider.create(request.getParameter("p"));
		String username = request.getParameter("u");
		OpenIdManager.instance().redirect(provider, username, request, response, getServletContext());
	}
}
