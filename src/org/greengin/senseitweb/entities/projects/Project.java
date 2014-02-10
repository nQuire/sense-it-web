package org.greengin.senseitweb.entities.projects;


import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Entity
public class Project {	

	public Project() {
		this.activity = null;
	}

	@Id
    @Column (name = "PROJECT_ID", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
	Long id;

	@Basic
	String title;

	@Basic
	String description;

	@Basic
	ProjectType type;
	
	@OneToOne(cascade=CascadeType.ALL)
	AbstractActivity activity;
	
	public Long getId() {
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
