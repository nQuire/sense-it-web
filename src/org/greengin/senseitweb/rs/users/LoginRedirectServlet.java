package org.greengin.senseitweb.rs.users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.greengin.senseitweb.rs.users.OpenIdManager.Provider;

public class LoginRedirectServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Provider provider = Provider.create(request.getParameter("p"));
		OpenIdManager.instance().redirect(provider, request, response, getServletContext());
	}
}
