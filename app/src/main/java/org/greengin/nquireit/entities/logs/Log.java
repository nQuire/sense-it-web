package org.greengin.nquireit.entities.logs;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.users.UserProfile;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    Long id;

    @Basic
    @Getter
    @Setter
    Date timestamp;

    @Basic
    @Getter
    @Setter
    String type;

    @Basic
    @Getter
    @Setter
    String path;

    @ManyToOne
    @Getter
    @Setter
    UserProfile actor;

    @ManyToOne
    @Getter
    @Setter
    AbstractEntity object;

    @ManyToOne
    @Getter
    @Setter
    UserProfile owner;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    String value;
}

