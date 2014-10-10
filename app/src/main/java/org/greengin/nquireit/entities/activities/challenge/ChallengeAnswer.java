package org.greengin.nquireit.entities.activities.challenge;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.admin.ReportedContent;
import org.hibernate.annotations.Parameter;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class ChallengeAnswer extends VotableEntity {

    @ManyToOne
    @Getter
    @Setter
    UserProfile author;

    @Basic
    @Getter
    @Setter
    Boolean published = false;

    @Basic
    @Getter
    @Setter
    Date date;


    @Lob
    @org.hibernate.annotations.Type(
            type = "org.hibernate.type.SerializableToBlobType",
            parameters = {@Parameter(name = "classname", value = "java.util.HashMap")}
    )
    @Getter
    @Setter
    @NonNull
    private Map<Long, String> fieldValues = new HashMap<Long, String>();


    @Override
    public String getReportedType(ContextBean context) {
        return "challengeIdea";
    }

    @Override
    public String getReportedPath(ContextBean context) {
        ChallengeActivity activity = context.getChallengeDao().findActivity(this);
        Project project = context.getProjectDao().findProject(activity);
        return formatReportedPath(project);
    }

    private String formatReportedPath(Project project) {
        return String.format("/project/%d/challenge", project.getId());
    }

    @Override
    public void createReportedInfo(ReportedContent info, ContextBean context) {
        ChallengeActivity activity = context.getChallengeDao().findActivity(this);
        Project project = context.getProjectDao().findProject(activity);
        String path = formatReportedPath(project);

        info.setPath(path);
        info.setAuthor(author);
        info.addInfo("Project", project.getTitle());
    }

}
