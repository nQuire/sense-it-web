package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.util.Vector;

import org.greengin.senseitweb.utils.TimeValue;

public class SenseItProcessedSeriesVariable {

	public Vector<TimeValue> values;
	public int index;
	public SenseItDataUnits units;
	public String name;
    public String type;

	SenseItProcessedSeriesVariable(int index, String name, Vector<TimeValue> values, String type) {
		this.index = index;
		this.values = values;
		this.units = new SenseItDataUnits();
		this.name = name;
        this.type = type;
	}

	SenseItProcessedSeriesVariable(int index, String name, String type) {
		this(index, name, new Vector<TimeValue>(), type);
	}

}
