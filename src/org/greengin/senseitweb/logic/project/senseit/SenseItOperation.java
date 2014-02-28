package org.greengin.senseitweb.logic.project.senseit;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum SenseItOperation {
	GET_X,
	GET_Y,
	GET_Z,
	REMOVE_CC,
	INTEGRATE,
	MIN,
	MAX;
	
	@JsonValue
    public String getValue() { return this.name().toLowerCase(); }

    @JsonCreator
    public static SenseItOperation create(String val) {
    	SenseItOperation[] units = SenseItOperation.values();
        for (SenseItOperation unit : units) {
            if (unit.getValue().equals(val)) {
                return unit;
            }
        }
        return GET_X;
    }
}
