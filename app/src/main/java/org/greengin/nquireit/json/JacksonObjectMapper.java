package org.greengin.nquireit.json;



import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.greengin.nquireit.entities.activities.base.BaseAnalysis;
import org.greengin.nquireit.entities.activities.challenge.ChallengeActivity;
import org.greengin.nquireit.entities.activities.challenge.ChallengeAnswer;
import org.greengin.nquireit.entities.activities.challenge.ChallengeOutcome;
import org.greengin.nquireit.entities.activities.senseit.SenseItSeries;
import org.greengin.nquireit.entities.activities.senseit.SensorInput;
import org.greengin.nquireit.entities.data.AbstractDataProjectItem;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.rating.ForumNode;
import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.rating.Vote;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.json.mixins.*;
import org.greengin.nquireit.logic.project.senseit.SenseItAnalysisRequest;
import org.springframework.stereotype.Component;


@Component
public class JacksonObjectMapper extends ObjectMapper {

    public JacksonObjectMapper() throws Exception {

        configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

        addMixIn(UserProfile.class, UserProfileMixIn.class);


        addMixIn(Project.class, ProjectMixIn.class);
        addMixIn(ChallengeActivity.class, ChallengeActivityMixIn.class);
        addMixIn(ChallengeAnswer.class, ChallengeAnswerMixIn.class);
        addMixIn(ChallengeOutcome.class, ChallengeOutcomeMixIn.class);

        addMixIn(VotableEntity.class, VotableEntityMixIn.class);
        addMixIn(Vote.class, VoteMixIn.class);
        addMixIn(Comment.class, CommentMixIn.class);

        addMixIn(AbstractDataProjectItem.class, AbstractDataProjectItemMixIn.class);

        addMixIn(BaseAnalysis.class, SenseItAnalysisMixIn.class);
        addMixIn(SenseItAnalysisRequest.class, SenseItAnalysisMixIn.class);
        addMixIn(SenseItSeries.class, SenseItSeriesMixIn.class);
        addMixIn(SensorInput.class, SensorInputMixIn.class);

        addMixIn(ForumNode.class, ForumNodeMixIn.class);
    }

    private void addMixIn(Class<?> object, Class<?> mixin) {
        addMixInAnnotations(object, mixin);
    }

}