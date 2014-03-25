package org.greengin.senseitweb.entities.activities.challenge;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.projects.AbstractActivity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Vector;

@Entity
public class ChallengeActivity extends AbstractActivity {

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @Getter
    @Setter
    @NonNull
    Collection<ChallengeField> fields = new Vector<ChallengeField>();

    @Basic
    @Getter
    @Setter
    Integer maxAnswers = 1;

    @Basic
    @Getter
    @Setter
    ChallengeActivityStage stage = ChallengeActivityStage.PROPOSAL;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @Getter
    @Setter
    @NonNull
    ChallengeOutcome outcome = new ChallengeOutcome();


}
