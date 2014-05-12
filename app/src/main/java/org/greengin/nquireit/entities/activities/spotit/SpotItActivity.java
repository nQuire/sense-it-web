package org.greengin.nquireit.entities.activities.spotit;


import org.greengin.nquireit.entities.activities.base.BaseAnalysis;
import org.greengin.nquireit.entities.data.DataCollectionActivity;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;

import javax.persistence.Entity;
import java.util.HashMap;

@Entity
public class SpotItActivity extends DataCollectionActivity<SpotItObservation, BaseAnalysis> {

    @Override
    public void loadProjectData(ContextBean context, Project project, UserProfile user, HashMap<String, Long> data) {
        data.put("observations", 0l);
    }
}
