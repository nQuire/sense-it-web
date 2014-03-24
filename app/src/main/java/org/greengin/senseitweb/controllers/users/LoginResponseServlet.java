package org.greengin.senseitweb.controllers.users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.greengin.senseitweb.logic.permissions.OpenIdManager;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Autowired
    OpenIdManager openIdManager;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        openIdManager.updateLoginStatus(request);
		response.sendRedirect("status.jsp");
	}
}
