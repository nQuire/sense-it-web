package org.greengin.senseitweb.logic.project.senseit.transformations.maths;

import java.util.Vector;

import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItOperation;
import org.greengin.senseitweb.utils.TimeValue;

public class Integrate implements SenseItOperation {

	@Override
	public boolean process(Vector<Vector<TimeValue>> inputs, Vector<TimeValue> result) {
		Vector<TimeValue> input = inputs.firstElement();

		if (input.size() > 0) {
			int vn = input.firstElement().v.length;
			float[] acc = new float[vn];
			for (int i = 0; i < vn; i++) {
				acc[i] = 0;
			}

			TimeValue lastIv = null;

			for (int i = 0; i < input.size(); i++) {
				TimeValue v1 = input.get(i);
				TimeValue iv = new TimeValue(v1.t, new float[vn]);

				if (i > 0) {
					TimeValue v0 = input.get(i - 1);
					for (int j = 0; j < vn; j++) {
						iv.v[j] = .0005f * (v1.v[j] + v0.v[j]) * (v1.t - v0.t) + lastIv.v[j];
					}
				} else {
					for (int j = 0; j < vn; j++) {
						iv.v[j] = 0;
					}
				}
				
				result.add(iv);
				lastIv = iv;
			}
		}

		return true;
	}
}
