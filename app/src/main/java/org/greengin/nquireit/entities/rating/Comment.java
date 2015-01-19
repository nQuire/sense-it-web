package org.greengin.nquireit.entities.rating;


import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.activities.senseit.SenseItSeries;
import org.greengin.nquireit.entities.activities.spotit.SpotItActivity;
import org.greengin.nquireit.entities.activities.spotit.SpotItObservation;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.admin.ReportedContent;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Date;


@Entity
public class Comment extends VotableEntity {

    @Basic
    @Getter
    @Setter
    Date date;

	@Lob
    @Getter
    @Setter
	String comment;

	@ManyToOne
    @Getter
    @Setter
	CommentThreadEntity target;

    @Override
    public String getReportedType(ContextBean context) {
        String threadType = target.getReportedType(context);
        return threadType == null ? null : String.format("comment:%s", threadType);
    }

    @Override
    public String getReportedPath(ContextBean context) {
        return target.getReportedPath(context);
    }

    @Override
    public void createReportedInfo(ReportedContent info, ContextBean context) {
        info.setAuthor(author);
        info.addInfo("Comment", comment);
        info.setPath(target.getReportedPath(context));
    }
}
