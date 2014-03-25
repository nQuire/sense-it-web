package org.greengin.senseitweb.entities.activities.senseit;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;


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

    @ManyToOne
    @Getter
    @Setter
    SenseItProfile profile;

}
