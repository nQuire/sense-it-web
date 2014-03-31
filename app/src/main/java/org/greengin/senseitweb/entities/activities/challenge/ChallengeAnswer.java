package org.greengin.senseitweb.entities.activities.challenge;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.voting.VotableEntity;
import org.hibernate.annotations.Parameter;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.HashMap;
import java.util.Map;

@Entity
public class ChallengeAnswer extends VotableEntity {

    @ManyToOne
    @Getter
    @Setter
    ChallengeActivity activity;

    @ManyToOne
    @Getter
    @Setter
    UserProfile author;

    @Basic
    @Getter
    @Setter
    Boolean published = false;


    @Lob
    @org.hibernate.annotations.Type(
            type = "org.hibernate.type.SerializableToBlobType",
            parameters = {@Parameter(name = "classname", value = "java.util.HashMap")}
    )
    @Getter
    @Setter
    @NonNull
    private Map<Long, String> fieldValues = new HashMap<Long, String>();

}
