package org.greengin.nquireit.logic.data;

import java.util.Collection;

import org.greengin.nquireit.entities.data.AbstractDataProjectItem;

public class NewDataItemResponse<T extends AbstractDataProjectItem> {
	
	Long newItemId;
	
	Collection<T> items;
	

	public Long getNewItemId() {
		return newItemId;
	}

	public void setNewItemId(Long newItemId) {
		this.newItemId = newItemId;
	}

	public Collection<T> getItems() {
		return items;
	}

	public void setItems(Collection<T> items) {
		this.items = items;
	}
	

}
