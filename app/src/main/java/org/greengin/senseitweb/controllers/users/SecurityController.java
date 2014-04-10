package org.greengin.senseitweb.controllers.users;

import org.greengin.senseitweb.logic.permissions.RegistrationResponse;
import org.greengin.senseitweb.logic.permissions.Status2Response;
import org.greengin.senseitweb.logic.permissions.UserServiceBean;
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



    @RequestMapping(method = RequestMethod.GET)
    public void login() {
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public Status2Response checkLogin(HttpServletRequest request, HttpServletResponse response) {
        return userServiceBean.status(request, response);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Status2Response performLogin(@RequestParam("j_username") String username, @RequestParam("j_password") String password, HttpServletRequest request, HttpServletResponse response) {
        return userServiceBean.login(username, password, request, response);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public RegistrationResponse performRegister(@RequestParam("j_username") String username, @RequestParam("j_password") String password, HttpServletRequest request, HttpServletResponse response) {
        return userServiceBean.createUser(username, password, request, response);
    }
}
