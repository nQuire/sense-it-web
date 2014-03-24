package org.greengin.senseitweb.logic.project.senseit.transformations.maths;

import java.util.Vector;

import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItOperation;
import org.greengin.senseitweb.utils.TimeValue;

public class GetZ implements SenseItOperation {

	@Override
	public boolean process(Vector<Vector<TimeValue>> inputs, Vector<TimeValue> result) {
		Vector<TimeValue> input = inputs.firstElement();
		for (TimeValue tv : input) {
			result.add(new TimeValue(tv.t, new float[] {tv.v[2]}));
		}
		
		return true;
	}

}
