package org.greengin.senseitweb.logic.data;

import org.greengin.senseitweb.entities.data.AbstractDataProjectItem;

public interface DataItemManipulator<T extends AbstractDataProjectItem> {
	public void onCreate(T newItem);
	public void onUpdate(T item);
	public void onDelete(T item);
}
