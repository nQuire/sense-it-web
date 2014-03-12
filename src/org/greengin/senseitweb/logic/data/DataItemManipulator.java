package org.greengin.senseitweb.logic.data;

import org.greengin.senseitweb.entities.data.AbstractDataProjectItem;
import org.greengin.senseitweb.entities.data.DataCollectionActivity;
import org.greengin.senseitweb.entities.projects.Project;

public interface DataItemManipulator<T extends DataCollectionActivity<?, ?>, E extends AbstractDataProjectItem> {
	public boolean onCreate(Project project, T activity, E newItem);
	public void onUpdate(Project project, T activity, E item);
	public void onDelete(Project project, T activity, E item);
}
