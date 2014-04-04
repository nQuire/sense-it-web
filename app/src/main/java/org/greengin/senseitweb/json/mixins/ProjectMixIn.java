package org.greengin.senseitweb.json.mixins;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.senseitweb.entities.rating.Comment;

import java.util.List;

public abstract class ProjectMixIn {
    @JsonIgnore List<Comment> comments;
}
