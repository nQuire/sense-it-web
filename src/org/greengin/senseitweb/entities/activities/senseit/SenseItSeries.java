package org.greengin.senseitweb.entities.activities.senseit;

import java.util.HashMap;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.greengin.senseitweb.entities.data.AbstractDataProjectItem;
import org.greengin.senseitweb.utils.TimeValue;

@Entity
public class SenseItSeries extends AbstractDataProjectItem {
	
	@Basic
	String title;
	
	@Lob
	HashMap<Long, String> sensors = new HashMap<Long, String>();
	
	@Lob
	HashMap<Long, Vector<TimeValue>> data = new HashMap<Long, Vector<TimeValue>>();

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public HashMap<Long, String> getSensors() {
		return sensors;
	}

	public void setSensors(HashMap<Long, String> sensors) {
		this.sensors = sensors;
	}

	public HashMap<Long, Vector<TimeValue>> getData() {
		return data;
	}

	public void setData(HashMap<Long, Vector<TimeValue>> data) {
		this.data = data;
	}
	
}
