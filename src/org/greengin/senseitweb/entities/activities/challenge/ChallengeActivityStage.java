package org.greengin.senseitweb.entities.activities.challenge;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;


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
