package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.util.Vector;

import org.greengin.senseitweb.utils.TimeValue;

public class SenseItProcessedSeriesVariable {
	
	public Vector<TimeValue> values;	
	public int index;
	
	
	SenseItProcessedSeriesVariable(int index, Vector<TimeValue> values) {
		this.index = index;
		this.values = values;
	}
	
	SenseItProcessedSeriesVariable(int index) {
		this(index, new Vector<TimeValue>());
	}
	
	
}
