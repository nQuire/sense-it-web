package org.greengin.senseitweb.controllers.users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.greengin.senseitweb.logic.permissions.OpenIdManager;
import org.greengin.senseitweb.logic.permissions.OpenIdManager.Provider;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginRedirectServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

    @Autowired
    private OpenIdManager openIdManager;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Provider provider = Provider.create(request.getParameter("p"));
		String username = request.getParameter("u");
        openIdManager.redirect(provider, username, request, response, getServletContext());
	}
}
