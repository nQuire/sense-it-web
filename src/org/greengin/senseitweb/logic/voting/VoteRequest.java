package org.greengin.senseitweb.logic.voting;

import org.greengin.senseitweb.entities.voting.Vote;

public class VoteRequest {
	
	Long value;

	String comment;

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public void update(Vote vote) {
		vote.setComment(comment);
		vote.setValue(value);
	}
}
