package org.greengin.senseitweb.entities.activities.challenge;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;

@Entity
public class ChallengeOutcome extends AbstractEntity {
	
	@OneToOne
    @Getter
    @Setter
    ChallengeAnswer selectedAnswer;
	
	@Lob
	@Basic
    @Getter
    @Setter
	String explanation;


}
