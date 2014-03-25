package org.greengin.senseitweb.entities;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "ENTITY_TYPE")
public abstract class AbstractEntity {
    @Id
    @Column(name = "ENTITY_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Getter
    Long id;

}
