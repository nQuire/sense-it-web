package org.greengin.nquireit.entities.activities.base;


import javax.persistence.Entity;

import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectMetadata;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;

import java.util.HashMap;

@Entity
public abstract class AbstractActivity extends AbstractEntity {

    public abstract void loadProjectData(ContextBean context, Project project, UserProfile user, HashMap<String, Long> data);
}
