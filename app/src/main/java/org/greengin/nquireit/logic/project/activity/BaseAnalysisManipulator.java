package org.greengin.nquireit.logic.project.activity;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.activities.base.BaseAnalysis;
import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.logic.data.DataItemManipulator;
import org.greengin.nquireit.logic.project.senseit.SenseItAnalysisRequest;

public class BaseAnalysisManipulator extends DataItemManipulator<SenseItActivity, BaseAnalysis> {

    @Getter
    @Setter
    String text;

    @Override
	public boolean onCreate(BaseAnalysis newItem) {
		onUpdate(newItem);
		return true;
	}

	@Override
	public void onUpdate(BaseAnalysis item) {
		if (getText() != null) {
			item.setText(this.getText());
		}
	}

}
