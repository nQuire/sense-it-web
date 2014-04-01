package org.greengin.senseitweb.logic.project.senseit.transformations.maths;

import java.util.Vector;

import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItOperation;
import org.greengin.senseitweb.utils.TimeValue;

public class Derivative implements SenseItOperation {

	@Override
	public boolean process(Vector<Vector<TimeValue>> inputs, Vector<TimeValue> result) {
		Vector<TimeValue> input = inputs.firstElement();

		if (input.size() > 0) {
			int vn = input.firstElement().v.length;

			TimeValue v0 = input.firstElement();

			for (int i = 1; i < input.size(); i++) {
				TimeValue v1 = input.get(i);
				long dt = v1.t - v0.t;
				if (dt > 0) {
					TimeValue dv = new TimeValue(v0.t, new float[vn]);
					for (int j = 0; j < vn; j++) {
						dv.v[j] = 1e3f * (v1.v[j] - v0.v[j]) / dt;  
					}
					result.add(dv);
					v0 = v1;
				}
			}
		}

		return true;
	}
}
