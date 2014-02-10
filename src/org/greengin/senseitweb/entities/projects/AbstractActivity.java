package org.greengin.senseitweb.entities.projects;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ACTIVITY_TYPE")
public abstract class AbstractActivity {
	
	@Id
    @Column (name = "ACTIVITY_ID", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
	Long id;

	
	public Long getId() {
		return id;
	}
}
