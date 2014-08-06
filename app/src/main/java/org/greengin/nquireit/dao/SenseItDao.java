package org.greengin.nquireit.dao;

import lombok.Getter;
import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.activities.senseit.SenseItProfile;
import org.greengin.nquireit.entities.activities.senseit.SenseItSeries;
import org.greengin.nquireit.entities.activities.senseit.SensorInput;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectMetadataBlock;
import org.greengin.nquireit.logic.project.senseit.SenseItProfileRequest;
import org.greengin.nquireit.logic.project.senseit.SensorInputRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Component
public class SenseItDao {


    @PersistenceContext
    @Getter
    EntityManager em;

    public SenseItSeries getSeries(Long dataId) {
        return em.find(SenseItSeries.class, dataId);
    }


    private void persistProfile(SenseItActivity activity) {
        if (activity.getProfile() == null) {
            activity.setProfile(new SenseItProfile());
        }

        em.persist(activity.getProfile());
    }


    @Transactional
    public void updateProfile(SenseItProfileRequest profileData, SenseItActivity activity) {
        persistProfile(activity);
        profileData.updateProfile(activity.getProfile());
    }

    @Transactional
    public void createSensor(SensorInputRequest inputData, SenseItActivity activity) {
        persistProfile(activity);
        SensorInput input = new SensorInput();
        activity.getProfile().getSensorInputs().add(input);
        inputData.updateInput(input);
        em.persist(input);
    }

    @Transactional
    public void updateSensor(SensorInputRequest inputData, Long inputId) {
        SensorInput input = em.find(SensorInput.class, inputId);
        inputData.updateInput(input);
        em.persist(input);
    }

    @Transactional
    public void deleteSensor(SenseItActivity activity, Long inputId) {
        SensorInput input = em.find(SensorInput.class, inputId);
        persistProfile(activity);
        activity.getProfile().getSensorInputs().remove(input);
    }

    public void initProject(Project project) {

        ProjectMetadataBlock link = new ProjectMetadataBlock();
        link.setTitle("Getting started");
        link.setContent("This mission uses " +
                "<a target=\"_blank\" " +
                "href=\"https://play.google.com/store/apps/details?id=org.greengin.sciencetoolkit&hl=en\">" +
                "Sense-it</a> to collect data. ");
        project.getMetadata().getBlocks().add(link);

        ProjectMetadataBlock addData = new ProjectMetadataBlock();
        addData.setTitle("Adding data");
        addData.setContent("To add data:" +
                "<ol>" +
                "<li>Run Sense-it on your Android device.</li>" +
                "<li>Activate this project (you need to join it of course!).</li>" +
                "<li>On the <code>RECORD</code> tab, touch <code>Start</code> to start recording data.</li>" +
                "<li>Touch <code>Stop</code> when your data series is complete.</li>" +
                "<li>Go to <code>SHARE</code> tab, then select this project to view all recorded data.</li>" +
                "<li>Touch the cloud icon to upload data to this server.</li>" +
                "</ol>");
        project.getMetadata().getBlocks().add(addData);
    }
}
