package org.greengin.nquireit.entities.logs;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class LogReport extends AbstractEntity {

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    String users;
}
