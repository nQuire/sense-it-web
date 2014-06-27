package org.greengin.nquireit.dao;


import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectDescription;
import org.greengin.nquireit.entities.projects.ProjectType;
import org.greengin.nquireit.entities.users.RoleType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.data.FileManagerBean;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.greengin.nquireit.logic.project.ProjectCreationRequest;
import org.greengin.nquireit.logic.project.metadata.ProjectRequest;
import org.greengin.nquireit.logic.users.SubscriptionManagerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

@Component
public class ProjectDao {

    static final String JOINED_PROJECTS_QUERY = "SELECT DISTINCT e FROM Role r INNER JOIN r.context e WHERE r.user=:user AND r.type=:access";

    static final String PROJECTS_QUERY = "SELECT p FROM Project p";
    static final String TYPED_PROJECTS_QUERY = "SELECT p FROM Project p WHERE p.type = :type";
    private static final String MY_PROJECTS_QUERY = "SELECT r, e FROM Role r INNER JOIN r.context e WHERE r.user = :user";


    @PersistenceContext
    EntityManager em;

    @Autowired
    SubscriptionManagerBean subscriptionManagerBean;

    @Autowired
    FileManagerBean fileManager;

    @Transactional
    public Long createProject(ProjectCreationRequest projectData, UserProfile author) {

        Project project = new Project();
        project.setTitle("New project");
        project.setOpen(false);
        project.setAuthor(author);
        projectData.initProject(project);
        em.persist(project);
        subscriptionManagerBean.projectCreatedInTransaction(em, project, author);
        return project.getId();
    }

    public Project project(Long projectId) {
        return em.find(Project.class, projectId);
    }

    @Transactional
    public void setOpen(Long projectId, Boolean open) {
        Project p = em.find(Project.class, projectId);
        p.setOpen(open);
    }

    @Transactional
    public Project updateMetadata(Long projectId, ProjectRequest data, FileMapUpload files) {
        Project project = project(projectId);

        project.setTitle(data.getTitle());
        if (project.getDescription() == null) {
            project.setDescription(new ProjectDescription());
        }

        project.getDescription().setTeaser(data.getDescription().getTeaser());

        if (files != null && files.getData().containsKey("image")) {
            FileMapUpload.FileData file = files.getData().get("image");
            String fileContext = projectId.toString();
            if (file == null) {
                project.getDescription().setImage(null);
            } else {
                try {
                    String filename = fileManager.uploadFile(fileContext, file.filename, file.data);
                    project.getDescription().setImage(filename);
                } catch (IOException exception) {
                    project.getDescription().setImage(null);
                }
            }
        } else {
            project.getDescription().setImage(data.getDescription().getImage());
        }

        project.getDescription().setBlocks(data.getDescription().getBlocks());

        return project;
    }

    public List<Project> getProjects() {
        TypedQuery<Project> query = em.createQuery(PROJECTS_QUERY, Project.class);
        return query.getResultList();
    }

    public List<Object[]> getMyProjects(UserProfile user) {
        TypedQuery<Object[]> query = em.createQuery(MY_PROJECTS_QUERY, Object[].class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    public List<Project> getProjectsSimple(String type) {
        TypedQuery<Project> query = em.createQuery(TYPED_PROJECTS_QUERY, Project.class);
        query.setParameter("type", ProjectType.create(type));

        return query.getResultList();
    }

    @Transactional
    public Boolean deleteProject(Project project) {
        em.remove(project);
        return true;
    }

    public List<Project> joinedProjects(UserProfile user) {
        Vector<Project> projects = new Vector<Project>();

        TypedQuery<AbstractEntity> query = em.createQuery(JOINED_PROJECTS_QUERY, AbstractEntity.class);
        query.setParameter("access", RoleType.MEMBER);
        query.setParameter("user", user);
        for (AbstractEntity context : query.getResultList()) {
            if (context instanceof Project) {
                projects.add((Project) context);
            }
        }
        return projects;
    }
}