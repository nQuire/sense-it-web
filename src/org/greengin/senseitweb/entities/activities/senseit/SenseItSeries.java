package org.greengin.senseitweb.entities.activities.senseit;

import java.util.HashMap;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.greengin.senseitweb.entities.data.AbstractDataProjectItem;
import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItOperations;
import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItProcessedSeries;
import org.greengin.senseitweb.utils.TimeValue;

@Entity
public class SenseItSeries extends AbstractDataProjectItem {
	
	@Basic
	String title;
	
	@Lob
	HashMap<Long, String> sensors = new HashMap<Long, String>();
	
	@Lob
	HashMap<Long, Vector<TimeValue>> data = new HashMap<Long, Vector<TimeValue>>();
	
	transient SenseItProcessedSeries processData = null;
	
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
	

	public HashMap<String, TimeValue> getVarValue() {
		processData();
		return SenseItOperations.tableVariables(processData, (SenseItActivity) this.dataStore);
	}
	
	public Vector<TimeValue> varData(String varId) {
		processData();
		return processData.values.get(varId);
	}
	
	private void processData() {
		if (processData == null) {
			processData = SenseItOperations.process(this.data, (SenseItActivity) this.dataStore);
		}
	}
	
}
