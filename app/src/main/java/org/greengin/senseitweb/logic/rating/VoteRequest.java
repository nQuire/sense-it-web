package org.greengin.senseitweb.logic.rating;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.rating.Vote;

public class VoteRequest {

    @Getter
    @Setter
    Long value;

    public void update(Vote vote) {
        vote.setValue(value);
    }
}
