package org.greengin.nquireit.logic.data;

import org.greengin.nquireit.entities.data.AbstractDataProjectItem;
import org.greengin.nquireit.entities.data.DataCollectionActivity;
import org.greengin.nquireit.entities.projects.Project;

public interface DataItemManipulator<T extends DataCollectionActivity<?, ?>, E extends AbstractDataProjectItem> {
	public boolean onCreate(Project project, T activity, E newItem);
	public void onUpdate(Project project, T activity, E item);
	public void onDelete(Project project, T activity, E item);
}
