package org.greengin.nquireit.logic.project.senseit.transformations.maths;

import java.util.Vector;

import org.greengin.nquireit.logic.project.senseit.transformations.SenseItOperation;
import org.greengin.nquireit.utils.TimeValue;

public class Max implements SenseItOperation {

	@Override
	public boolean process(Vector<Vector<TimeValue>> inputs, Vector<TimeValue> result) {
		Vector<TimeValue> input = inputs.firstElement();
		
		float max = Float.NEGATIVE_INFINITY;
		TimeValue maxtv = null;
		
		for (TimeValue tv : input) {
			if (tv.v[0] > max) {
				maxtv = tv;
				max = tv.v[0];
			}
		}
		
		if (maxtv != null) {
			result.add(maxtv);
		}
		
		return true;
	}

}
