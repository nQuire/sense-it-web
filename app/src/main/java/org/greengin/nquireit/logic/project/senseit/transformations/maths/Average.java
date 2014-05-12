package org.greengin.nquireit.logic.project.senseit.transformations.maths;

import org.greengin.nquireit.logic.project.senseit.transformations.SenseItOperation;
import org.greengin.nquireit.utils.TimeValue;

import java.util.Arrays;
import java.util.Vector;

public class Average implements SenseItOperation {

	@Override
	public boolean process(Vector<Vector<TimeValue>> inputs, Vector<TimeValue> result) {
		Vector<TimeValue> input = inputs.firstElement();

		int n = input.size() > 0 ? input.get(0).v.length : 0;
        TimeValue avg = new TimeValue(0, new float[n]);
        Arrays.fill(avg.v, 0);

		for (TimeValue tv : input) {
            for (int i = 0; i < n; i++) {
                avg.v[i] += tv.v[i];
            }
		}

        if (input.size() > 0) {
            for (int i = 0; i < n; i++) {
                avg.v[i] /= input.size();
            }
        }

        result.add(avg);
		return true;
	}

}
