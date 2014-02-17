package org.greengin.senseitweb.rs;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.voting.VotableEntity;
import org.greengin.senseitweb.json.mixins.ProjectMixIn;
import org.greengin.senseitweb.json.mixins.UserProfileMixIn;
import org.greengin.senseitweb.json.mixins.VotableEntityMixIn;

@Provider
@Produces("application/json")
public class JacksonObjectMapper implements ContextResolver<ObjectMapper> {
	
	private ObjectMapper objectMapper;

	public JacksonObjectMapper() throws Exception {
		this.objectMapper = new ObjectMapper();
		
		//objectMapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
		
		addMixIn(Project.class, ProjectMixIn.class);
		addMixIn(VotableEntity.class, VotableEntityMixIn.class);
		addMixIn(UserProfile.class, UserProfileMixIn.class);
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