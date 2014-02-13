package org.greengin.senseitweb.entities.activities.challenge;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.greengin.senseitweb.entities.projects.AbstractActivity;

@Entity
public class ChallengeActivity extends AbstractActivity {
	
	@OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
	Collection<ChallengeField> fields;
	
	@Basic
	Integer maxAnswers = 1;
	
	
	public Collection<ChallengeField> getFields() {
		return fields;
	}

	public void setFields(Collection<ChallengeField> fields) {
		this.fields = fields;
	}

	public Integer getMaxAnswers() {
		return maxAnswers;
	}

	public void setMaxAnswers(Integer maxAnswers) {
		this.maxAnswers = maxAnswers;
	}
	
	
}
