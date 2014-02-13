package org.greengin.senseitweb.entities.activities.challenge;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;


public enum ChallengeFieldType {
	TEXTFIELD,
	TEXTAREA,
	TITLE;
	
	@JsonValue
    public String getValue() { return this.name().toLowerCase(); }

    @JsonCreator
    public static ChallengeFieldType create(String val) {
    	ChallengeFieldType[] units = ChallengeFieldType.values();
        for (ChallengeFieldType unit : units) {
            if (unit.getValue().equals(val)) {
                return unit;
            }
        }
        return TEXTFIELD;
    }
}
