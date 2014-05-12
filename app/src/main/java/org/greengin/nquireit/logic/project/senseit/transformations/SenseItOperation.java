package org.greengin.nquireit.logic.project.senseit.transformations;

import java.util.Vector;

import org.greengin.nquireit.utils.TimeValue;

public interface SenseItOperation {
	boolean process(Vector<Vector<TimeValue>> inputs, Vector<TimeValue> result);
}
