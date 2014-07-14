package org.greengin.nquireit.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class WeightedEntity extends AbstractEntity implements Comparable<WeightedEntity> {
    @Basic
    @Getter
    @Setter
    int weight = 0;


    @Override
    public int compareTo( WeightedEntity o) {
        return weight - o.weight;
    }
}
