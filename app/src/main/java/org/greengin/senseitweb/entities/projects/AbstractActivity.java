package org.greengin.senseitweb.entities.projects;


import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.greengin.senseitweb.entities.AbstractEntity;

@Entity
public abstract class AbstractActivity extends AbstractEntity {
}
