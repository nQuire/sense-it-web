package org.greengin.nquireit.entities.projects;


import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.activities.base.AbstractActivity;
import org.greengin.nquireit.entities.rating.CommentThreadEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.admin.ReportedContent;

import javax.persistence.*;


@Entity
public class Project extends CommentThreadEntity {

    public Project() {
        this.activity = null;
    }

    @Basic
    @Getter
    @Setter
    String title;

    @ManyToOne
    @Getter
    @Setter
    UserProfile author;

    @Getter
    @Setter
    @Embedded
    ProjectMetadata metadata;

    @Basic
    @Getter
    @Setter
    ProjectType type;

    @Basic
    @Getter
    @Setter
    Boolean open = false;

    @Basic
    @Getter
    @Setter
    boolean featured = false;


    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @Getter
    @Setter
    AbstractActivity activity;

    @Override
    public String getReportedType(ContextBean context) {
        return "project";
    }

    @Override
    public String getReportedPath(ContextBean context) {
        return String.format("/project/%d", getId());
    }

    @Override
    public UserProfile getOwner(ContextBean context) {
        return author;
    }

    @Override
    public void createReportedInfo(ReportedContent info, ContextBean context) {
        info.setAuthor(author);
        info.setPath(getReportedPath(context));
        info.addInfo("Title", title);

        String typeStr = null;
        switch(type) {
            case SENSEIT:
                typeStr = "Sense it";
                break;
            case CHALLENGE:
                typeStr = "Win it";
                break;
            case SPOTIT:
                typeStr = "Spot it";
                break;
        }
        info.addInfo("Type", typeStr);
    }
}
