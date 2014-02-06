package org.greengin.senseitweb.entities;


import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.datanucleus.jpa.annotations.Extension;


@Entity
public class Project {
	

	public Project() {
		this.activity = null;
	}

	@Id
    @Column (nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Extension (vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
   	String id;

	@Basic
	String title;

	@Basic
	String description;

	@Basic
	ProjectType type;
	
	@OneToOne(fetch = FetchType.EAGER, optional=false, cascade=CascadeType.PERSIST)
	AbstractActivity activity;
	
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProjectType getType() {
		return type;
	}

	public void setType(ProjectType type) {
		this.type = type;
	}
	
	

	public AbstractActivity getActivity() {
		return activity;
	}
	
	public void setActivity(AbstractActivity activity) {
		this.activity = activity;
	}

}
