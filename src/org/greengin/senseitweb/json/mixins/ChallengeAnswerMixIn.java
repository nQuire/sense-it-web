package org.greengin.senseitweb.json.mixins;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.greengin.senseitweb.entities.projects.Project;

public abstract class ChallengeAnswerMixIn {
	@JsonIgnore abstract Project getProject();
}
