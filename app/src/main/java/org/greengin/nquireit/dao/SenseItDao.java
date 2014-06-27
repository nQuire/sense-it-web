package org.greengin.nquireit.dao;

import lombok.Getter;
import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.activities.senseit.SenseItProfile;
import org.greengin.nquireit.entities.activities.senseit.SenseItSeries;
import org.greengin.nquireit.entities.activities.senseit.SensorInput;
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

    private SenseItProfile profile(SenseItActivity activity) {
        if (activity.getProfile() == null) {
            activity.setProfile(new SenseItProfile());
        }

        return activity.getProfile();
    }

    private SensorInput input(Long id) {
        return em.find(SensorInput.class, id);
    }

    @Transactional
    public void updateProfile(SenseItProfileRequest profileData, SenseItActivity activity) {
        profileData.updateProfile(profile(activity));
    }

    @Transactional
    public void createSensor(SensorInputRequest inputData, SenseItActivity activity) {
        SensorInput input = new SensorInput();
        profile(activity).getSensorInputs().add(input);
        inputData.updateInput(input);
    }

    @Transactional
    public void updateSensor(SensorInputRequest inputData, Long inputId) {
        SensorInput input = input(inputId);
        inputData.updateInput(input);
    }

    @Transactional
    public void deleteSensor(SenseItActivity activity, Long inputId) {
        profile(activity).getSensorInputs().remove(input(inputId));
    }
}
