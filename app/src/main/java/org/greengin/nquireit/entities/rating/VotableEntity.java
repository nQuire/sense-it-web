package org.greengin.nquireit.entities.rating;

import java.util.Collection;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.admin.ReportedContent;
import org.greengin.nquireit.logic.moderation.ModerationStatus;
import org.greengin.nquireit.logic.rating.VoteCount;

@Entity
public abstract class VotableEntity extends AbstractEntity {

    @OneToMany(mappedBy = "target", orphanRemoval = true, cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    @NonNull
    Collection<Vote> votes = new Vector<Vote>();

    @Basic
    @Getter
    @Setter
    ModerationStatus moderationStatus = ModerationStatus.UNMODERATED;


    public VoteCount getVoteCount() {
        return new VoteCount(getVotes(), ContextBean.getContext().getUsersManager().currentUser());
    }

    public abstract String getReportedType(ContextBean context);
    public abstract String getReportedPath(ContextBean context);
    public abstract UserProfile getOwner(ContextBean context);
    public abstract void createReportedInfo(ReportedContent info, ContextBean context);
}
