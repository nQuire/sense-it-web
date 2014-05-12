package org.greengin.nquireit.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.nquireit.entities.activities.challenge.ChallengeOutcome;

public abstract class ChallengeActivityMixIn {
	@JsonIgnore
    abstract ChallengeOutcome getOutcome();
}
