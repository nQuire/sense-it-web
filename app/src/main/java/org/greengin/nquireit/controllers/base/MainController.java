package org.greengin.nquireit.controllers.base;

import org.greengin.nquireit.logic.ContextBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by evilfer on 8/11/14.
 */

@Controller
public class MainController {

    @Autowired
    ContextBean contextBean;

    @RequestMapping(value = "/api/text", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> texts(HttpServletRequest request) {

        return contextBean.getTextDao().getTexts();
    }


}
