package org.greengin.nquireit.entities.activities.base;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.data.AbstractDataProjectItem;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.admin.ReportedContent;


@Entity
public class BaseAnalysis extends AbstractDataProjectItem {

	@Lob
	@Basic
    @Getter
    @Setter
    String text;


    @Override
    public String getReportedType(ContextBean context) {
        return null;
    }

    @Override
    public String getReportedPath(ContextBean context) {
        return null;
    }

    @Override
    public void createReportedInfo(ReportedContent info, ContextBean context) {
    }
}
