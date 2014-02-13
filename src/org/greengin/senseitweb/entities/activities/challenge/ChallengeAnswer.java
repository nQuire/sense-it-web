package org.greengin.senseitweb.entities.activities.challenge;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;

@Entity
public class ChallengeAnswer {
	
	@Id
	@Column(name = "ANSWER_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne
	Project project;
	
	@ManyToOne
	UserProfile author;
	
	@Basic
	Boolean published = false;
		

	@Basic
	@ElementCollection
	@JoinTable(name="FIELD_VALUES", joinColumns = @JoinColumn(name="ID"))
	@MapKeyColumn (name="FIELD_ID")
	@Column(name="VALUE")
	private Map<Long, String> fieldValues = new HashMap<Long, String>();
	
	
	public Long getId() {
		return id;
	}

	public UserProfile getAuthor() {
		return author;
	}

	public void setAuthor(UserProfile author) {
		this.author = author;
	}

	public Map<Long, String> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(Map<Long, String> fieldValues) {
		this.fieldValues = fieldValues;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

}
