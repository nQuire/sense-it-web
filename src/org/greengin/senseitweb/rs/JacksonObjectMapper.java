package org.greengin.senseitweb.rs;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.voting.VotableEntity;
import org.greengin.senseitweb.entities.voting.Vote;
import org.greengin.senseitweb.json.mixins.ChallengeActivityMixIn;
import org.greengin.senseitweb.json.mixins.ChallengeAnswerMixIn;
import org.greengin.senseitweb.json.mixins.ProjectMixIn;
import org.greengin.senseitweb.json.mixins.UserProfileMixIn;
import org.greengin.senseitweb.json.mixins.VotableEntityMixIn;
import org.greengin.senseitweb.json.mixins.VoteMixIn;

@Provider
@Produces("application/json")
public class JacksonObjectMapper implements ContextResolver<ObjectMapper> {
	
	private ObjectMapper objectMapper;

	public JacksonObjectMapper() throws Exception {
		this.objectMapper = new ObjectMapper();
		
		addMixIn(UserProfile.class, UserProfileMixIn.class);
		
		addMixIn(Project.class, ProjectMixIn.class);
		addMixIn(ChallengeActivity.class, ChallengeActivityMixIn.class);
		addMixIn(ChallengeAnswer.class, ChallengeAnswerMixIn.class);
		
		addMixIn(VotableEntity.class, VotableEntityMixIn.class);
		addMixIn(Vote.class, VoteMixIn.class);
	}
	
	private void addMixIn(Class<?> object, Class<?> mixin) {
		objectMapper.getSerializationConfig().addMixInAnnotations(object, mixin);
		objectMapper.getDeserializationConfig().addMixInAnnotations(object, mixin);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return this.objectMapper;
	}
}