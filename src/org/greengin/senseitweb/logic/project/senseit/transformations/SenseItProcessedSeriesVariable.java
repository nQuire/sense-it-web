package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.util.Vector;

import org.greengin.senseitweb.utils.TimeValue;

public class SenseItProcessedSeriesVariable {

	public Vector<TimeValue> values;
	public int index;
	public SenseItDataUnits units;

	SenseItProcessedSeriesVariable(int index, Vector<TimeValue> values) {
		this.index = index;
		this.values = values;
		this.units = new SenseItDataUnits();
	}

	SenseItProcessedSeriesVariable(int index) {
		this(index, new Vector<TimeValue>());
	}

}
