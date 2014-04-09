package org.greengin.senseitweb.json;



import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeOutcome;
import org.greengin.senseitweb.entities.activities.senseit.SenseItAnalysis;
import org.greengin.senseitweb.entities.activities.senseit.SenseItSeries;
import org.greengin.senseitweb.entities.activities.senseit.SensorInput;
import org.greengin.senseitweb.entities.data.AbstractDataProjectItem;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.rating.Comment;
import org.greengin.senseitweb.entities.users.OpenIdEntity;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.rating.VotableEntity;
import org.greengin.senseitweb.entities.rating.Vote;
import org.greengin.senseitweb.json.mixins.*;
import org.greengin.senseitweb.logic.project.senseit.SenseItAnalysisRequest;
import org.springframework.stereotype.Component;


@Component
public class JacksonObjectMapper extends ObjectMapper {

    public JacksonObjectMapper() throws Exception {

        configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

        addMixIn(UserProfile.class, UserProfileMixIn.class);
        addMixIn(OpenIdEntity.class, OpenIdEntityMixIn.class);


        addMixIn(Project.class, ProjectMixIn.class);
        addMixIn(ChallengeActivity.class, ChallengeActivityMixIn.class);
        addMixIn(ChallengeAnswer.class, ChallengeAnswerMixIn.class);
        addMixIn(ChallengeOutcome.class, ChallengeOutcomeMixIn.class);

        addMixIn(VotableEntity.class, VotableEntityMixIn.class);
        addMixIn(Vote.class, VoteMixIn.class);
        addMixIn(Comment.class, CommentMixIn.class);

        addMixIn(AbstractDataProjectItem.class, AbstractDataProjectItemMixIn.class);

        addMixIn(SenseItAnalysis.class, SenseItAnalysisMixIn.class);
        addMixIn(SenseItAnalysisRequest.class, SenseItAnalysisMixIn.class);
        addMixIn(SenseItSeries.class, SenseItSeriesMixIn.class);
        addMixIn(SensorInput.class, SensorInputMixIn.class);
    }

    private void addMixIn(Class<?> object, Class<?> mixin) {
        addMixInAnnotations(object, mixin);
    }

}