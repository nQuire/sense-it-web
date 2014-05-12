package org.greengin.nquireit.utils;

import java.io.Serializable;

public class TimeValue implements Serializable {
	
	private static final long serialVersionUID = -6920689192427126965L;
	
	
	public final long t;
	public final float[] v;

	public TimeValue(long t, float[] v) {
		this.t = t;
		this.v = v;
	}

	public float[] getV() {
		return v;
	}

	public long getT() {
		return t;
	}
}