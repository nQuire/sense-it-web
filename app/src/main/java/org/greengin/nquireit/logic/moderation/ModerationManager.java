package org.greengin.nquireit.logic.moderation;

import javax.persistence.EntityManager;

import org.greengin.nquireit.entities.rating.VotableEntity;
import org.springframework.transaction.annotation.Transactional;

public class ModerationManager {
    EntityManager em;
    VotableEntity target;

    public ModerationManager(EntityManager em, VotableEntity target) {
        this.em = em;
        this.target = target;
    }

    @Transactional
    public VotableEntity moderate(ModerationStatus status) {

        target.setModerationStatus(status);

        return target;
    }
}
