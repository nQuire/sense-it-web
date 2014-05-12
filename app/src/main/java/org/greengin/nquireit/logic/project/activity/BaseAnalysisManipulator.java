package org.greengin.nquireit.logic.project.activity;

import org.greengin.nquireit.entities.activities.base.BaseAnalysis;
import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.logic.data.DataItemManipulator;
import org.greengin.nquireit.logic.project.senseit.SenseItAnalysisRequest;

public class BaseAnalysisManipulator extends SenseItAnalysisRequest implements DataItemManipulator<SenseItActivity, BaseAnalysis> {

	@Override
	public boolean onCreate(Project project, SenseItActivity activity, BaseAnalysis newItem) {
		onUpdate(project, activity, newItem);
		return true;
	}

	@Override
	public void onUpdate(Project project, SenseItActivity activity, BaseAnalysis item) {
		if (getText() != null) {
			item.setText(this.getText());
		}
	}

	@Override
	public void onDelete(Project project, SenseItActivity activity, BaseAnalysis item) {
	}

}
