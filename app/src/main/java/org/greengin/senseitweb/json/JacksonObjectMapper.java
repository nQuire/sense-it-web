package org.greengin.senseitweb.json;


import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.entities.activities.senseit.SenseItAnalysis;
import org.greengin.senseitweb.entities.activities.senseit.SenseItSeries;
import org.greengin.senseitweb.entities.data.AbstractDataProjectItem;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.voting.VotableEntity;
import org.greengin.senseitweb.entities.voting.Vote;
import org.greengin.senseitweb.json.mixins.ChallengeActivityMixIn;
import org.greengin.senseitweb.json.mixins.ChallengeAnswerMixIn;
import org.greengin.senseitweb.json.mixins.AbstractDataProjectItemMixIn;
import org.greengin.senseitweb.json.mixins.ProjectMixIn;
import org.greengin.senseitweb.json.mixins.SenseItAnalysisMixIn;
import org.greengin.senseitweb.json.mixins.SenseItSeriesMixIn;
import org.greengin.senseitweb.json.mixins.UserProfileMixIn;
import org.greengin.senseitweb.json.mixins.VotableEntityMixIn;
import org.greengin.senseitweb.json.mixins.VoteMixIn;
import org.greengin.senseitweb.logic.project.senseit.SenseItAnalysisRequest;


public class JacksonObjectMapper extends ObjectMapper {

    public JacksonObjectMapper() {
        SimpleModule module = new SimpleModule("JSONModule", new Version(2, 0, 0, null, null, null));

        module.setMixInAnnotation(UserProfile.class, UserProfileMixIn.class);

        module.setMixInAnnotation(Project.class, ProjectMixIn.class);
        module.setMixInAnnotation(ChallengeActivity.class, ChallengeActivityMixIn.class);
        module.setMixInAnnotation(ChallengeAnswer.class, ChallengeAnswerMixIn.class);

        module.setMixInAnnotation(VotableEntity.class, VotableEntityMixIn.class);
        module.setMixInAnnotation(Vote.class, VoteMixIn.class);

        module.setMixInAnnotation(AbstractDataProjectItem.class, AbstractDataProjectItemMixIn.class);

        module.setMixInAnnotation(SenseItAnalysis.class, SenseItAnalysisMixIn.class);
        module.setMixInAnnotation(SenseItAnalysisRequest.class, SenseItAnalysisMixIn.class);
        module.setMixInAnnotation(SenseItSeries.class, SenseItSeriesMixIn.class);

        registerModule(module);
    }
}