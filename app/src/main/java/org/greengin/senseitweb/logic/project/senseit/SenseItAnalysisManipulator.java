package org.greengin.senseitweb.logic.project.senseit;

import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItAnalysis;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.data.DataItemManipulator;

public class SenseItAnalysisManipulator extends SenseItAnalysisRequest implements DataItemManipulator<SenseItActivity, SenseItAnalysis> {

	@Override
	public boolean onCreate(Project project, SenseItActivity activity, SenseItAnalysis newItem) {
		onUpdate(project, activity, newItem);
		return true;
	}

	@Override
	public void onUpdate(Project project, SenseItActivity activity, SenseItAnalysis item) {
		if (getText() != null) {
			item.setText(this.getText());
		}
	}

	@Override
	public void onDelete(Project project, SenseItActivity activity, SenseItAnalysis item) {
	}

}
