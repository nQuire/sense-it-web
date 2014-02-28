package org.greengin.senseitweb.logic.project.senseit;

import org.greengin.senseitweb.entities.activities.senseit.SenseItAnalysis;
import org.greengin.senseitweb.logic.data.DataItemManipulator;

public class SenseItAnalysisManipulator extends SenseItAnalysisRequest implements DataItemManipulator<SenseItAnalysis> {

	@Override
	public void onCreate(SenseItAnalysis newItem) {
		onUpdate(newItem);
	}

	@Override
	public void onUpdate(SenseItAnalysis item) {
		if (getText() != null) {
			item.setText(this.getText());
		}

		if (getTx() != null) {
			item.setTx(getTx());
		}

	}

	@Override
	public void onDelete(SenseItAnalysis item) {
	}

}
