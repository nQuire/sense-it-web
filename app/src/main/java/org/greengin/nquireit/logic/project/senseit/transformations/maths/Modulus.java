package org.greengin.nquireit.logic.project.senseit.transformations.maths;

import java.util.Vector;

import org.greengin.nquireit.logic.project.senseit.transformations.SenseItOperation;
import org.greengin.nquireit.utils.TimeValue;

public class Modulus implements SenseItOperation {

	@Override
	public boolean process(Vector<Vector<TimeValue>> inputs, Vector<TimeValue> result) {
		Vector<TimeValue> input = inputs.firstElement();
		for (TimeValue v : input) {
			float m = 0f;
			for (int i = 0; i < v.v.length; i++) {
				m += v.v[i] * v.v[i];
			}
			
			m = (float) Math.sqrt(m);
			
			TimeValue mtv = new TimeValue(v.t, new float[] {m});
			result.add(mtv);
		}

		return true;
	}
}
