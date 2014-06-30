package org.greengin.nquireit.json.mixins;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.rating.ForumNode;
import org.greengin.nquireit.entities.rating.ForumThread;
import org.greengin.nquireit.json.Views;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.List;

public abstract class ForumNodeMixIn {
    @JsonIgnore abstract ForumNode getParent();
    @JsonView(Views.ForumList.class) abstract String getChildren();
    @JsonView(Views.ForumNode.class) abstract String getThreads();
}
