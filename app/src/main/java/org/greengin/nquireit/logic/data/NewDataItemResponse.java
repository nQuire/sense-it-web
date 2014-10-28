package org.greengin.nquireit.logic.data;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.data.AbstractDataProjectItem;

public class NewDataItemResponse<T extends AbstractDataProjectItem> {

    @Getter
    @Setter
    Long newItemId;

    @Getter
    @Setter
    Collection<T> items;

}
