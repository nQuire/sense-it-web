package org.greengin.nquireit.entities;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@DiscriminatorColumn(name = "ENTITY_TYPE")
public abstract class AbstractEntity {
    @Id
    @Column(name = "ENTITY_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Getter
    Long id;

    public boolean equals(Object obj) {
        return obj != null && obj instanceof AbstractEntity && ((AbstractEntity) obj).getId().equals(getId());
    }
}
