package org.greengin.senseitweb.entities.activities.challenge;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ChallengeActivityStage {
	PROPOSAL,
	VOTING,
	OUTCOME;
	
	@JsonValue
    public String getValue() { return this.name().toLowerCase(); }

    @JsonCreator
    public static ChallengeActivityStage create(String val) {
    	ChallengeActivityStage[] stages = ChallengeActivityStage.values();
        for (ChallengeActivityStage stage : stages) {
            if (stage.getValue().equals(val)) {
                return stage;
            }
        }
        return PROPOSAL;
    }
}
