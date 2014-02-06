package org.greengin.senseitweb.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.datanucleus.jpa.annotations.Extension;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ACTIVITY_TYPE")
public abstract class AbstractActivity {
	
	@Id
    @Column (nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Extension (vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
   	String id;

	
	public String getId() {
		return id;
	}
}
