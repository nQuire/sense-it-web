package org.greengin.nquireit.entities.projects;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.OrderColumn;
import java.util.List;

@Embeddable
public class ProjectMetadata {
    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    String teaser;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    String outline;

    @Getter
    @Setter
    String image;

    @ElementCollection
    @OrderColumn
    @Getter
    @Setter
    List<ProjectMetadataBlock> blocks;

    public boolean containsKeyword(String keyword) {
        if (outline != null && StringUtils.containsIgnoreCase(outline, keyword)) {
            return true;
        } else if (teaser != null && StringUtils.containsIgnoreCase(teaser, keyword)) {
            return true;
        } else if (blocks != null) {
            for (ProjectMetadataBlock b : blocks) {
                if (b.containsKeyword(keyword)) {
                    return true;
                }
            }
        }

        return false;
    }
}
