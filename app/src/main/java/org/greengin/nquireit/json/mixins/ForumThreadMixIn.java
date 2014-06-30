package org.greengin.nquireit.json.mixins;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.rating.ForumNode;
import org.greengin.nquireit.json.Views;

import java.util.List;

public abstract class ForumThreadMixIn {
    //@JsonIgnore List<Comment> comments;
    @JsonView(Views.ForumThread.class) abstract ForumNode getForum();
}
