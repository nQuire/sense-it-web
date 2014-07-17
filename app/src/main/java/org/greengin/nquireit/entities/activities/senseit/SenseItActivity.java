package org.greengin.nquireit.entities.activities.senseit;



import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.nquireit.entities.activities.base.BaseAnalysis;
import org.greengin.nquireit.entities.data.DataCollectionActivity;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectMetadata;
import org.greengin.nquireit.entities.projects.ProjectMetadataBlock;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.senseit.SenseItActivityActions;

import java.util.HashMap;

@Entity
public class SenseItActivity extends DataCollectionActivity<SenseItSeries, BaseAnalysis> {

	

	@OneToOne(orphanRemoval = true, cascade=CascadeType.ALL)
    @Getter
    @Setter
    @NonNull
    SenseItProfile profile = new SenseItProfile();

    @Override
    public void loadProjectData(ContextBean context, Project project, UserProfile user, HashMap<String, Long> data) {
        data.put("data", new SenseItActivityActions(context, project.getId(), user, true).getDataCount());
    }

}
