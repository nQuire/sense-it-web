package org.greengin.nquireit.entities.activities.challenge;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.nquireit.entities.activities.base.AbstractActivity;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

@Entity
public class ChallengeActivity extends AbstractActivity {

    public static final int DEFAULT_MAX = 1;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @Getter
    @Setter
    @NonNull
    Collection<ChallengeField> fields = new Vector<ChallengeField>();

    @Basic
    @Getter
    @Setter
    Integer maxAnswers = DEFAULT_MAX;

    @Basic
    @Getter
    @Setter
    ChallengeActivityStage stage = ChallengeActivityStage.PROPOSAL;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @Getter
    @Setter
    ChallengeOutcome outcome = new ChallengeOutcome();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    @NonNull
    List<ChallengeAnswer> answers = new Vector<ChallengeAnswer>();

    @Override
    public void loadProjectData(ContextBean context, Project project, UserProfile user, HashMap<String, Long> data) {

    }
}
