package org.greengin.nquireit.controllers.forum;


import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.entities.rating.ForumNode;
import org.greengin.nquireit.entities.rating.ForumThread;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.forum.ForumManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/api/forum")
public class ForumController {

    @Autowired
    ContextBean contextBean;

    private ForumManager createForumManager(HttpServletRequest request) {
        return new ForumManager(contextBean, request);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.ForumList.class)
    public ForumNode list(HttpServletRequest request) {
        return createForumManager(request).getRoot();
    }

    @RequestMapping(value = "/{forumId}/threads", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.ForumThread.class)
    public ForumNode threads(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "/threads/{threadId}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.ForumThread.class)
    public ForumThread thread(HttpServletRequest request) {
        return null;
    }


    /** admin options **/


}
