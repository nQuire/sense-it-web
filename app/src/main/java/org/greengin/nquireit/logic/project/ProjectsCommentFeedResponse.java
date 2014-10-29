package org.greengin.nquireit.logic.project;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.rating.Comment;

/**
 * Created by evilfer on 10/29/14.
 */
public class ProjectsCommentFeedResponse {
    @Getter
    @Setter
    String comment;

    @Getter
    @Setter
    String projectTitle;

    @Getter
    @Setter
    Long projectId;

    public ProjectsCommentFeedResponse(Comment comment) {
        Project project = (Project) comment.getTarget();
        this.comment = comment.getComment();
        this.projectTitle = project.getTitle();
        this.projectId = project.getId();
    }
}
