package org.greengin.nquireit.logic.project.spotit;


import lombok.Getter;
import lombok.Setter;

public class SpotItObservationRequest {

    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    String description;

    @Getter
    @Setter
    String geolocation;

    @Getter
    @Setter
    long date;

}
