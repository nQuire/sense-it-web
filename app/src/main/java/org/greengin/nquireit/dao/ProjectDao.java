package org.greengin.nquireit.dao;


import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.activities.base.AbstractActivity;
import org.greengin.nquireit.entities.projects.*;
import org.greengin.nquireit.entities.users.Role;
import org.greengin.nquireit.entities.users.RoleType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.data.FileManagerBean;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.greengin.nquireit.logic.project.MyProjectResponse;
import org.greengin.nquireit.logic.project.ProjectCreationRequest;
import org.greengin.nquireit.logic.project.UserProjectListResponse;
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
    static final String PROJECTS_TYPE_QUERY = "SELECT p FROM Project p WHERE p.type = :type";
    static final String PROJECTS_TYPE_FEATURED_QUERY = "SELECT p FROM Project p WHERE p.featured = TRUE AND p.type = :type";
    static final String PROJECTS_FEATURED_QUERY = "SELECT p FROM Project p WHERE p.featured = TRUE";

    static final String TYPED_PROJECTS_QUERY = "SELECT p FROM Project p WHERE p.type = :type";
    private static final String MY_PROJECTS_QUERY = "SELECT r, e FROM Role r INNER JOIN r.context e WHERE r.user = :user";

    private static final String FIND_PROJECT_QUERY = "SELECT p FROM Project p WHERE p.activity = :activity";


    @PersistenceContext
    EntityManager em;

    @Autowired
    SubscriptionManagerBean subscriptionManagerBean;

    @Autowired
    FileManagerBean fileManager;

    @Autowired
    ContextBean context;

    @Transactional
    public Project createProject(ProjectCreationRequest projectData, UserProfile author) {

        Project project = new Project();
        project.setTitle("New project");
        project.setOpen(false);
        project.setAuthor(author);
        projectData.initProject(project, context);
        em.persist(project);
        subscriptionManagerBean.projectCreatedInTransaction(em, project, author);
        return project;
    }

    public Project project(Long projectId) {
        return em.find(Project.class, projectId);
    }

    public Project findProject(AbstractActivity activity) {
        TypedQuery<Project> query = em.createQuery(FIND_PROJECT_QUERY, Project.class);
        query.setParameter("activity", activity);
        return query.getSingleResult();
    }

    @Transactional
    public void setOpen(Project project, Boolean open) {
        em.persist(project);
        project.setOpen(open);
    }

    @Transactional
    public Project updateMetadata(Project project, ProjectRequest data, FileMapUpload files) {
        em.persist(project);

        project.setTitle(data.getTitle());
        if (project.getMetadata() == null) {
            project.setMetadata(new ProjectMetadata());
        }


        if (files != null && files.getData().containsKey("image")) {
            FileMapUpload.FileData file = files.getData().get("image");
            String fileContext = project.getId().toString();
            if (file == null) {
                project.getMetadata().setImage(null);
            } else {
                try {
                    String filename = fileManager.uploadFile(fileContext, file.filename, file.data);
                    project.getMetadata().setImage(filename);
                } catch (IOException exception) {
                    project.getMetadata().setImage(null);
                }
            }
        } else {
            project.getMetadata().setImage(data.getMetadata().getImage());
        }

        project.getMetadata().setTeaser(data.getMetadata().getTeaser());
        project.getMetadata().setOutline(data.getMetadata().getOutline());
        project.getMetadata().setBlocks(data.getMetadata().getBlocks());

        return project;
    }

    public List<Project> getProjects() {
        TypedQuery<Project> query = em.createQuery(PROJECTS_QUERY, Project.class);
        return query.getResultList();
    }

    public List<Project> getProjects(ProjectType type, boolean featured) {
        TypedQuery<Project> query;

        if (type != null) {
            query = em.createQuery(featured ? PROJECTS_TYPE_FEATURED_QUERY : PROJECTS_TYPE_QUERY, Project.class);
            query.setParameter("type", type);
        } else {
            query = em.createQuery(featured ? PROJECTS_FEATURED_QUERY : PROJECTS_QUERY, Project.class);
        }

        return query.getResultList();
    }


    public List<Object[]> getMyProjects(UserProfile user) {
        TypedQuery<Object[]> query = em.createQuery(MY_PROJECTS_QUERY, Object[].class);
        query.setParameter("user", user);
        return query.getResultList();
    }


    public UserProjectListResponse getMyProjects(UserProfile user, boolean joined, boolean created) {
        UserProjectListResponse response = new UserProjectListResponse(joined, created);
        List<Object[]> all = context.getProjectDao().getMyProjects(user);

        for (Object[] entry : all) {
            if (entry.length == 2 && entry[0] instanceof Role && entry[1] instanceof Project) {
                Role r = (Role) entry[0];
                Project p = (Project) entry[1];

                if (created && r.getType() == RoleType.ADMIN) {
                    response.getAdmin().add(new MyProjectResponse(p));
                } else if (joined && r.getType() == RoleType.MEMBER) {
                    response.getMember().add(new MyProjectResponse(p));
                }
            }
        }

        return response;
    }

    public List<Project> getProjectsSimple(String type) {
        TypedQuery<Project> query = em.createQuery(TYPED_PROJECTS_QUERY, Project.class);
        query.setParameter("type", ProjectType.create(type));

        return query.getResultList();
    }

    @Transactional
    public Boolean deleteProject(Project project) {
        em.persist(project);
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


    @Transactional
    public void setFeatured(Long projectId, boolean featured) {
        Project p = em.find(Project.class, projectId);
        p.setFeatured(featured);
    }

    @Transactional
    public void updateDataModel() {
    }
}