package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.util.Vector;

import org.greengin.senseitweb.utils.TimeValue;

public class SenseItProcessedSeriesVariable {

	public Vector<TimeValue> values;
	public int index;
	public SenseItDataUnits units;
	public String name;

	SenseItProcessedSeriesVariable(int index, String name, Vector<TimeValue> values) {
		this.index = index;
		this.values = values;
		this.units = new SenseItDataUnits();
		this.name = name;
	}

	SenseItProcessedSeriesVariable(int index, String name) {
		this(index, name, new Vector<TimeValue>());
	}

}
