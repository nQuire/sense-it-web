package org.greengin.senseitweb.logic.project;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.AbstractContentEditor;
import org.greengin.senseitweb.permissions.PermissionsManager;

public class ProjectEditor extends AbstractContentEditor {

	protected Long projectId;
	protected Project project;

	public ProjectEditor(Long projectId, HttpServletRequest request) {
		super(request);
		this.projectId = projectId;
		this.project = em.find(Project.class, projectId);
		this.hasAccess = PermissionsManager.get().canEditProject(project, user);
	}

	public Project updateMetadata(ProjectRequest data) {
		if (hasAccess) {
			em.getTransaction().begin();
			data.updateProject(project);
			em.getTransaction().commit();

			return em.find(Project.class, projectId);
		} else {
			return null;
		}
	}

	public boolean deleteProject() {
		if (hasAccess) {
			em.getTransaction().begin();
			em.remove(project);
			em.getTransaction().commit();
			return true;
		} else {
			return false;
		}
	}
}
