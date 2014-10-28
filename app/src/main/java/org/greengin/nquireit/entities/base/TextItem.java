package org.greengin.nquireit.entities.base;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.AbstractEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class TextItem extends AbstractEntity {

    @Basic
    @Getter
    @Setter
    String textId;

    @Lob
    @Getter
    @Setter
    String content;
}
