package org.greengin.nquireit.entities.activities.challenge;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.hibernate.annotations.Parameter;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class ChallengeAnswer extends VotableEntity {

    @ManyToOne
    @Getter
    @Setter
    UserProfile author;

    @Basic
    @Getter
    @Setter
    Boolean published = false;

    @Basic
    @Getter
    @Setter
    Date date;


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
