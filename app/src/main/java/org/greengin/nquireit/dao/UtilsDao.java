package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.WeightedEntity;

import java.util.Collections;
import java.util.List;

public class UtilsDao {


    public <T extends WeightedEntity> void move(List<T> list, Long id, boolean up) {
        Collections.sort(list);
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                position = i;
                break;
            }
        }

        if (position >= 0) {
            int other = position + (up ? -1 : 1);
            Collections.swap(list, position, other);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setWeight(i);
            }
        }
    }
}
