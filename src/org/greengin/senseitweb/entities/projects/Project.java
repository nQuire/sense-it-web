package org.greengin.senseitweb.entities.projects;


import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.greengin.senseitweb.entities.IdEntity;


@Entity
public class Project extends IdEntity {	

	public Project() {
		this.activity = null;
	}


	@Basic
	String title;

	@Basic
	String description;

	@Basic
	ProjectType type;
	
	@Basic
	Boolean open = false;
	
	@OneToOne(orphanRemoval = true, cascade=CascadeType.ALL)
	AbstractActivity activity;
	

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

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

}
