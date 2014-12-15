package org.greengin.nquireit.entities.projects;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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

    public boolean containsKeyword(String keyword) {
        return (title != null && StringUtils.containsIgnoreCase(title, keyword)) ||
                (content != null && StringUtils.containsIgnoreCase(content, keyword));
    }
}
