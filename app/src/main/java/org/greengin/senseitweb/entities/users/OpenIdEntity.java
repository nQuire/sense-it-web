package org.greengin.senseitweb.entities.users;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class OpenIdEntity extends AbstractEntity {

    @Basic
    @Getter
    @Setter
    String openId;

    @Basic
    @Getter
    @Setter
    String email;

}
