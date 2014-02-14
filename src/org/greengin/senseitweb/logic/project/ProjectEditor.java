package org.greengin.senseitweb.logic.project;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.AbstractContentEditor;
import org.greengin.senseitweb.logic.permissions.PermissionsManager;

public class ProjectEditor extends AbstractContentEditor {

	protected Long projectId;
	protected Project project;
	protected boolean projectEditable;

	public ProjectEditor(Long projectId, HttpServletRequest request) {
		super(request);
		this.projectId = projectId;
		this.project = em.find(Project.class, projectId);
		this.hasAccess = PermissionsManager.get().canEditProject(project, user);
		this.projectEditable = hasAccess && !project.getOpen();
	}
	
	public Project setOpen(Boolean open) {
		if (hasAccess) {
			em.getTransaction().begin();
			project.setOpen(open);
			em.getTransaction().commit();
			
			return project;
		} else {
			return null;
		}
	}


	public Project updateMetadata(ProjectRequest data) {
		if (projectEditable) {
			em.getTransaction().begin();
			data.updateProject(project);
			em.getTransaction().commit();

			return project;
		} else {
			return null;
		}
	}

	public boolean deleteProject() {
		if (projectEditable) {
			em.getTransaction().begin();
			em.remove(project);
			em.getTransaction().commit();
			return true;
		} else {
			return false;
		}
	}
}
