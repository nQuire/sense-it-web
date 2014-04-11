package org.greengin.senseitweb.controllers.users;

import org.greengin.senseitweb.logic.users.RegistrationResponse;
import org.greengin.senseitweb.logic.users.StatusResponse;
import org.greengin.senseitweb.logic.users.UserServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/api/security")
public class SecurityController {

    @Autowired
    UserServiceBean userServiceBean;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public StatusResponse checkLogin(HttpServletRequest request) {
        return userServiceBean.status(request.getSession());
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public StatusResponse performLogin(@RequestParam("j_username") String username, @RequestParam("j_password") String password, HttpServletRequest request, HttpServletResponse response) {
        return userServiceBean.login(username, password, request, response);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public StatusResponse performLogout(HttpServletRequest request, HttpServletResponse response) {
        return userServiceBean.logout(request, response);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public RegistrationResponse performRegister(@RequestParam("j_username") String username, @RequestParam("j_password") String password, HttpServletRequest request, HttpServletResponse response) {
        return userServiceBean.registerUser(username, password, request, response);
    }
}
