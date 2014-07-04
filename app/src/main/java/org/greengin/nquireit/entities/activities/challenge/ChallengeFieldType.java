package org.greengin.nquireit.entities.activities.challenge;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ChallengeFieldType {
	TEXTFIELD,
	TEXTAREA,
	TITLE,
	TEXTANGULAR;

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
