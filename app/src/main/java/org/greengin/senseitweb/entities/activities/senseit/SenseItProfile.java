package org.greengin.senseitweb.entities.activities.senseit;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Vector;


@Entity
public class SenseItProfile extends AbstractEntity {

    @Basic
    @Getter
    @Setter
    Boolean geolocated = false;


    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @Getter
    @Setter
    @NonNull
    Collection<SensorInput> sensorInputs = new Vector<SensorInput>();

    @Lob
    @Getter
    @Setter
    @NonNull
    SenseItTransformations tx = new SenseItTransformations();


    public SensorInput inputById(Long id) {
        for (SensorInput input : sensorInputs) {
            if (input.getId().equals(id)) {
                return input;
            }
        }
        return null;
    }
}
