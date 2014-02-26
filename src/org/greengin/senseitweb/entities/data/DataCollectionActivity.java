package org.greengin.senseitweb.entities.data;

import javax.persistence.Entity;
import org.greengin.senseitweb.entities.projects.AbstractActivity;

@Entity
public abstract class DataCollectionActivity<T extends DataItem> extends AbstractActivity implements DataStore<T>{
	
}
