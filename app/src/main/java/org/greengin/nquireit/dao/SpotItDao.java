package org.greengin.nquireit.dao;

import lombok.Getter;
import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.activities.senseit.SenseItProfile;
import org.greengin.nquireit.entities.activities.senseit.SenseItSeries;
import org.greengin.nquireit.entities.activities.senseit.SensorInput;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectMetadata;
import org.greengin.nquireit.entities.projects.ProjectMetadataBlock;
import org.greengin.nquireit.logic.project.senseit.SenseItProfileRequest;
import org.greengin.nquireit.logic.project.senseit.SensorInputRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Component
public class SpotItDao {


    @PersistenceContext
    @Getter
    EntityManager em;


    public void initProject(Project project) {

        ProjectMetadataBlock link = new ProjectMetadataBlock();
        link.setTitle("Mission goal");
        link.setContent("This mission is for people to share images about a specific topic.");
        project.getMetadata().getBlocks().add(link);

        ProjectMetadataBlock process = new ProjectMetadataBlock();
        process.setTitle("How to participate");
        process.setContent("<ul>" +
                "<li>Take a picture related to the project.</li>" +
                "<li>Upload it in the <code>Upload picture</code> tab above.</li>");

        project.getMetadata().getBlocks().add(process);
    }
}
