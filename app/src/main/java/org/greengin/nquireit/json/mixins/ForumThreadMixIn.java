package org.greengin.nquireit.json.mixins;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.rating.ForumNode;
import org.greengin.nquireit.json.Views;

import java.util.List;

public abstract class ForumThreadMixIn {
    @JsonView(Views.ForumThread.class) abstract List<Comment> getComments();
    @JsonView(Views.ForumThread.class) abstract ForumNode getForum();
    @JsonView({Views.ForumNode.class, Views.ForumList.class}) abstract Comment getLastComment();
    @JsonView(Views.ForumNode.class) abstract Comment getFirstComment();
    @JsonView(Views.ForumNode.class) abstract int getCommentCount();
}
