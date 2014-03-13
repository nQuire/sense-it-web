package org.greengin.senseitweb.logic.project.senseit.transformations.maths;

import java.util.Vector;

import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItOperation;
import org.greengin.senseitweb.utils.TimeValue;

public class Min implements SenseItOperation {

	@Override
	public boolean process(Vector<Vector<TimeValue>> inputs, Vector<TimeValue> result) {
		Vector<TimeValue> input = inputs.firstElement();
		
		float min = Float.POSITIVE_INFINITY;
		TimeValue mintv = null;
		
		for (TimeValue tv : input) {
			if (tv.v[0] < min) {
				mintv = tv;
				min = tv.v[0];
			}
		}
		
		if (mintv != null) {
			result.add(mintv);
		}
		
		return true;
	}

}
