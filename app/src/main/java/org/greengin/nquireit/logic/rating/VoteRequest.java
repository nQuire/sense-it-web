package org.greengin.nquireit.logic.rating;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.rating.Vote;

public class VoteRequest {

    @Getter
    @Setter
    Long value;

    public void update(Vote vote) {
        vote.setValue(Math.min(1, Math.max(-2, value)));
    }
}
