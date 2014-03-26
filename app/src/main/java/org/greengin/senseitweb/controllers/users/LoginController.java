package org.greengin.senseitweb.controllers.users;

import org.greengin.senseitweb.logic.permissions.OpenIdManager;
import org.greengin.senseitweb.logic.permissions.OpenIdManager.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private OpenIdManager openIdManager;

    @RequestMapping(value = "/login/redirect", method = RequestMethod.GET)
    protected void redirect(@RequestParam("p") String provider,
                            @RequestParam(value = "u", required = false) String username,
                            HttpServletRequest request, HttpServletResponse response) {
        openIdManager.redirect(Provider.create(provider), username, request, response);
    }


    @RequestMapping(value = "/login/formredirection")
    public String formRedirection(HttpServletRequest request, Model model) {
        model.addAttribute("parameterMap", request.getAttribute("parameterMap"));
        model.addAttribute("message", request.getAttribute("message"));
        return "formredirection";
    }

    @RequestMapping(value = "/login/response")
    public String loginResponse(HttpServletRequest request, Model model) {
        openIdManager.updateLoginStatus(request);
        model.addAttribute("email", openIdManager.getEmail(request));
        return "status";
    }


}
