package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.util.Vector;

import org.greengin.senseitweb.utils.TimeValue;

public interface SenseItOperation {
	boolean process(Vector<Vector<TimeValue>> inputs, Vector<TimeValue> result);
}
