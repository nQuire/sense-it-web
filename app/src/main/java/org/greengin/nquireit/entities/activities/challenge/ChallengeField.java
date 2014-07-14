package org.greengin.nquireit.entities.activities.challenge;

import javax.persistence.Basic;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.WeightedEntity;


@Entity
public class ChallengeField extends WeightedEntity {


    @Basic
    @Getter
    @Setter
    ChallengeFieldType type;

    @Basic
    @Getter
    @Setter
    String label;


}
