package org.greengin.senseitweb.logic.project.challenge;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeField;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.project.AbstractActivityEditor;

public class ChallengeActivityEditor extends AbstractActivityEditor<ChallengeActivity> {
	
	public ChallengeActivityEditor(Long projectId, HttpServletRequest request) {
		super(projectId, request, ChallengeActivity.class);
	}
	
	
	public Project createField(ChallengeFieldRequest fieldData) {
		if (hasAccess) {
			em.getTransaction().begin();
			ChallengeField field = new ChallengeField();
			fieldData.update(field);
			activity.getFields().add(field);
			em.getTransaction().commit();

			return project;		
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

			return project;		
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
			
			return project;		
		} else {
			return null;
		}
	}
	
}
