package org.greengin.nquireit.logic.data;

import lombok.Getter;
import org.greengin.nquireit.entities.data.AbstractDataProjectItem;
import org.greengin.nquireit.entities.data.DataCollectionActivity;
import org.greengin.nquireit.entities.projects.Project;

public abstract class DataItemManipulator<T extends DataCollectionActivity<?, ?>, E extends AbstractDataProjectItem> {
    @Getter
    protected Project project;

    @Getter
    protected T activity;

    public void init(Project project, T activity) {
        this.project = project;
        this.activity = activity;
    }

	public abstract boolean onCreate(E newItem);
	public abstract void onUpdate(E item);
}
