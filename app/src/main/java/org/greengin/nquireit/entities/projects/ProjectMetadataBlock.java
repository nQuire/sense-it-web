package org.greengin.nquireit.entities.projects;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProjectMetadataBlock {
    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    String content;
}
