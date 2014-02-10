package org.greengin.senseitweb.entities.activities.challenge;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.greengin.senseitweb.entities.projects.AbstractActivity;

@Entity
public class ChallengeActivity extends AbstractActivity {
	
	@OneToMany(cascade=CascadeType.ALL)
	Collection<ChallengeField> fields;
	
	
	
	public Collection<ChallengeField> getFields() {
		return fields;
	}

	public void setFields(Collection<ChallengeField> fields) {
		this.fields = fields;
	}
	
	
}
