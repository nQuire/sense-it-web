package org.greengin.senseitweb.json.mixins;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeOutcome;

public abstract class SenseItSeriesMixIn extends AbstractDataProjectItemMixIn {
	@JsonIgnore abstract ChallengeOutcome getData();
	
}
