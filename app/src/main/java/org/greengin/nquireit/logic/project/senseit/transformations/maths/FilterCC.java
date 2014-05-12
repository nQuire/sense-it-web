package org.greengin.nquireit.logic.project.senseit.transformations.maths;

import java.util.Vector;

import org.greengin.nquireit.logic.project.senseit.transformations.SenseItOperation;
import org.greengin.nquireit.utils.TimeValue;

public class FilterCC implements SenseItOperation {

	@Override
	public boolean process(Vector<Vector<TimeValue>> inputs, Vector<TimeValue> result) {
		Vector<TimeValue> input = inputs.firstElement();
		
		if (input.size() > 0) {
			int vn = input.firstElement().v.length;
			float[] avg = new float[vn];
			for (int i = 0; i < vn; i++) {
				avg[i] = 0;
			}
			
			for (TimeValue tv : input) {
				for (int i = 0; i < vn; i++) {
					avg[i] += tv.v[i];
				}	
			}

			for (int i = 0; i < vn; i++) {
				avg[i] /= input.size();
			}	
			
			for (TimeValue tv : input) {
				TimeValue ftv = new TimeValue(tv.t, new float[vn]);
				for (int i = 0; i < vn; i++) {
					ftv.v[i] = tv.v[i] - avg[i];
				}
				
				result.add(ftv);
			}
		}
		
		return true;
	}
}
