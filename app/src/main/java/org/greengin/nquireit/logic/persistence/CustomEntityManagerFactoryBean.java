package org.greengin.nquireit.logic.persistence;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;

public class CustomEntityManagerFactoryBean extends LocalContainerEntityManagerFactoryBean {


    private EntityManager entityManager;

    public CustomEntityManagerFactoryBean() {
        entityManager = null;
    }

    public EntityManager createEntityManager() {
        if (entityManager == null) {
            entityManager = this.getNativeEntityManagerFactory().createEntityManager();
        }
        return entityManager;
    }
}
