package org.greengin.nquireit.entities.activities.spotit;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.activities.base.AbstractActivity;
import org.greengin.nquireit.entities.data.AbstractDataProjectItem;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.admin.ReportedContent;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class SpotItObservation extends AbstractDataProjectItem {

    @Basic
    @Getter
    @Setter
    String title;

    @Basic
    @Getter
    @Setter
    String geolocation;

    @Basic
    @Getter
    @Setter
    String observation;

    @Override
    public String getReportedType(ContextBean context) {
        return "spotitObservation";
    }

    @Override
    public String getReportedPath(ContextBean context) {
        AbstractActivity activity = this.dataStore;
        Project project = context.getProjectDao().findProject(activity);
        return String.format("/project/%d/item/%d", project.getId(), getId());
    }

    @Override
    public void createReportedInfo(ReportedContent info, ContextBean context) {
        info.setPath(getReportedPath(context));
        info.setAuthor(getAuthor());

        info.addInfo("Title", title);
        String link = String.format("<a href=\"/files/image/%s\" target=\"_blank\">" +
                "<img src=\"/files/thunb/%s\"/></a>", observation, observation);
        info.addInfo("Image", link);
    }
}
