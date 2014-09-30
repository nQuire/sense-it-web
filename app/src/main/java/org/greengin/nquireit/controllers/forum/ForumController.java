package org.greengin.nquireit.controllers.forum;


import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.rating.ForumNode;
import org.greengin.nquireit.entities.rating.ForumThread;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.forum.ForumManager;
import org.greengin.nquireit.logic.forum.ForumRequest;
import org.greengin.nquireit.logic.project.metadata.ProjectRequest;
import org.greengin.nquireit.logic.rating.CommentRequest;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @RequestMapping(value = "/{forumId}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.ForumNode.class)
    public ForumNode threads(@PathVariable("forumId") Long forumId, HttpServletRequest request) {
        return createForumManager(request).getNode(forumId);
    }

    @RequestMapping(value = "/thread/{threadId}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.ForumThread.class)
    public ForumThread thread(@PathVariable("threadId") Long threadId, HttpServletRequest request) {
        return createForumManager(request).getThread(threadId);
    }


    /** admin options **/

    @RequestMapping(value = "/{containerId}/children", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.ForumList.class)
    public ForumNode createForum(@PathVariable("containerId") Long containerId, @RequestBody ForumRequest forumData, HttpServletRequest request) {
        return createForumManager(request).createForum(containerId, forumData);
    }

    @RequestMapping(value = "/{forumId}", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(value = Views.ForumList.class)
    public ForumNode updateForum(@PathVariable("forumId") Long forumId, @RequestBody ForumRequest forumData, HttpServletRequest request) {
        return createForumManager(request).updateForum(forumId, forumData);
    }

    /** participant options **/



    @RequestMapping(value = "/{forumId}/threads", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.ForumList.class)
    public Long createThread(@PathVariable("forumId") Long forumId, @RequestBody ForumRequest forumData, HttpServletRequest request) {
        return createForumManager(request).createThread(forumId, forumData);
    }

    @RequestMapping(value = "/{threadId}/comments", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.ForumThread.class)
    public ForumThread commentsPost(@PathVariable("threadId") Long threadId, @RequestBody CommentRequest data, HttpServletRequest request) {
        return createForumManager(request).comment(threadId, data);
    }

    @RequestMapping(value = "/{threadId}/comments/{commentId}/vote", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public VoteCount acommentsVote(@PathVariable("threadId") Long threadId, @PathVariable("commentId") Long commentId, @RequestBody VoteRequest voteData, HttpServletRequest request) {
        return createForumManager(request).voteComment(threadId, commentId, voteData);
    }





}
