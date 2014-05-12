package org.greengin.nquireit.entities.activities.spotit;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.data.AbstractDataProjectItem;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class SpotItObservation extends AbstractDataProjectItem {

    @Basic
    @Getter
    @Setter
    String title;

    @Basic
    @Getter
    @Setter
    String geolocation;

    @Basic
    @Getter
    @Setter
    String observation;

    @Basic
    @Getter
    @Setter
    String description;

}
