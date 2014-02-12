package org.greengin.senseitweb.logic.projects.challenge;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeField;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.logic.projects.ProjectEditor;

public class ChallengeActivityEditor extends ProjectEditor {

	ChallengeActivity activity;
	
	public ChallengeActivityEditor(Long projectId, HttpServletRequest request) {
		super(projectId, request);
		
		if (project.getType() == ProjectType.CHALLENGE && project.getActivity() instanceof ChallengeActivity) {
			activity = (ChallengeActivity) project.getActivity();
		} else {
			hasAccess = false;
		}
	}
	
	
	public Project createField(ChallengeFieldRequest fieldData) {
		if (hasAccess) {
			em.getTransaction().begin();
			ChallengeField field = new ChallengeField();
			fieldData.update(field);
			activity.getFields().add(field);
			em.getTransaction().commit();

			return em.find(Project.class, projectId);			
		} else {
			return null;
		}
	}
	
	public Project updateField(Long fieldId, ChallengeFieldRequest fieldData) {
		if (hasAccess) {
			ChallengeField field = em.find(ChallengeField.class, fieldId);

			em.getTransaction().begin();
			fieldData.update(field);
			em.getTransaction().commit();

			return em.find(Project.class, projectId);
		} else {
			return null;
		}			
	}
	
	public Project deleteField(Long fieldId) {
		if (hasAccess) {
			ChallengeField field = em.find(ChallengeField.class, fieldId);

			em.getTransaction().begin();
			activity.getFields().remove(field);
			em.getTransaction().commit();
			
			return em.find(Project.class, projectId);			
		} else {
			return null;
		}
	}
	
}
