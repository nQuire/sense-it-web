package org.greengin.senseitweb.entities;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;



public enum ProjectType {
	SENSEIT;
	
	@JsonValue
    public String getValue() { return this.name().toLowerCase(); }

    @JsonCreator
    public static ProjectType create(String val) {
    	ProjectType[] units = ProjectType.values();
        for (ProjectType unit : units) {
            if (unit.getValue().equals(val)) {
                return unit;
            }
        }
        return SENSEIT;
    }
}
