package org.greengin.senseitweb.entities.projects;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProjectType {
	SENSEIT,
	CHALLENGE;
	
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
        return CHALLENGE;
    }
}
