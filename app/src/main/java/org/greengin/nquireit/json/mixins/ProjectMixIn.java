package org.greengin.nquireit.json.mixins;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.nquireit.entities.rating.Comment;

import java.util.List;

public abstract class ProjectMixIn {
    @JsonIgnore List<Comment> comments;
}
