package org.greengin.nquireit.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.nquireit.entities.projects.Project;

public abstract class ChallengeAnswerMixIn {
	@JsonIgnore
    abstract Project getProject();
}
