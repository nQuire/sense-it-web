package org.greengin.senseitweb.entities.activities.challenge;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.voting.VotableEntity;

@Entity
public class ChallengeAnswer extends VotableEntity {
	
	@ManyToOne
    @Getter
    @Setter
    Project project;
	
	@ManyToOne
    @Getter
    @Setter
	UserProfile author;
	
	@Basic
    @Getter
    @Setter
	Boolean published = false;
		

	@Lob
	@Basic
	@ElementCollection
	@JoinTable(name="FIELD_VALUES", joinColumns = @JoinColumn(name="ID"))
	@MapKeyColumn (name="FIELD_ID")
	@Column(name="VALUE")
    @Getter
    @Setter
    @NonNull
	private Map<Long, String> fieldValues = new HashMap<Long, String>();

}
