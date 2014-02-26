package org.greengin.senseitweb.entities.data;



import java.util.Collection;


public interface DataStore<T extends DataItem> {
	Collection<T> getData();	
}
