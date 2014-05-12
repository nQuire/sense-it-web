package org.greengin.nquireit.entities.activities.senseit;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.AbstractEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;


@Entity
public class SensorInput extends AbstractEntity {

    @Basic
    @Getter
    @Setter
    float rate;

    @Basic
    @Getter
    @Setter
    String sensor;

}
