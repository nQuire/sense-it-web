package org.greengin.senseitweb.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.senseitweb.entities.projects.Project;

public abstract class ChallengeAnswerMixIn {
	@JsonIgnore
    abstract Project getProject();
}
