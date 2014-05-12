package org.greengin.nquireit.logic.project.senseit.transformations.maths;

import java.util.Vector;

import org.greengin.nquireit.logic.project.senseit.transformations.SenseItOperation;
import org.greengin.nquireit.utils.TimeValue;

public class Abs implements SenseItOperation {

	@Override
	public boolean process(Vector<Vector<TimeValue>> inputs, Vector<TimeValue> result) {
		Vector<TimeValue> input = inputs.firstElement();
		
		for (TimeValue tv : input) {
			TimeValue abs = new TimeValue(tv.t,  new float[tv.v.length]);
			for (int i = 0; i < tv.v.length; i++) {
				abs.v[i] = Math.abs(tv.v[i]);
			}
			result.add(abs);
		}
		
		return true;
	}

}
