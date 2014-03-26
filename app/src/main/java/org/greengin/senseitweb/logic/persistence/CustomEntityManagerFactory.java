package org.greengin.senseitweb.logic.persistence;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;

public class CustomEntityManagerFactory extends LocalContainerEntityManagerFactoryBean {

    public EntityManager createEntityManager() {
        return this.getNativeEntityManagerFactory().createEntityManager();
    }
}
