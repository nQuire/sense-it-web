package org.greengin.nquireit.logic.rating;

import java.util.Collection;

import lombok.Getter;
import org.greengin.nquireit.entities.rating.Vote;
import org.greengin.nquireit.entities.users.UserProfile;

public class VoteCount {

    @Getter
	int positive;

    @Getter
	int negative;

    @Getter
	int total;

    @Getter
    boolean reported;

    @Getter
    Vote myVote;
	
	
	public VoteCount(Collection<Vote> votes, UserProfile selectedVoteAuthor) {
		positive = 0;
		negative = 0;
		myVote = null;
        reported = false;
		
		for (Vote vote : votes) {
			if (vote.getValue() > 0) {
				positive++;
			} else if (vote.getValue() == -1) {
				negative++;
			} else if (vote.getValue() == -2) {
                reported = true;
            }
			
			if (selectedVoteAuthor != null && selectedVoteAuthor.getId().equals(vote.getUser().getId())) {
				myVote = vote;
			}
		}

        total = positive + negative;
	}

}
