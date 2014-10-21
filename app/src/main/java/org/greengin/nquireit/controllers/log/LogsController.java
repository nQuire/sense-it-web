package org.greengin.nquireit.controllers.log;

import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.log.LogManager;
import org.greengin.nquireit.logic.log.PageViewLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/api/log")
public class LogsController {

    @Autowired
    ContextBean context;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Boolean pageView(@RequestBody PageViewLogRequest data, HttpServletRequest request) {
        LogManager manager = new LogManager(context, request);
        return manager.pageView(data.getPath(), request.getRequestedSessionId());
    }


}
