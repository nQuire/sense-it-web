package org.greengin.senseitweb.entities.activities.senseit;

import java.util.HashMap;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.data.AbstractDataProjectItem;
import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItOperations;
import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItProcessedSeries;
import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItProcessedSeriesVariable;
import org.greengin.senseitweb.utils.TimeValue;

@Entity
public class SenseItSeries extends AbstractDataProjectItem {
	
	@Basic
    @Getter
    @Setter
    String title;

	@Basic
    @Getter
    @Setter
	String geolocation;

	@Lob
    @Getter
    @Setter
    @NonNull
	HashMap<Long, String> sensors = new HashMap<Long, String>();
	
	@Lob
    @Getter
    @Setter
    @NonNull
	HashMap<Long, Vector<TimeValue>> data = new HashMap<Long, Vector<TimeValue>>();
	
	transient SenseItProcessedSeries processData = null;


	public HashMap<String, TimeValue> getVarValue(SenseItOperations ops) {
		processData(ops);
		return SenseItOperations.tableVariables(processData, (SenseItActivity) this.dataStore);
	}
	
	public SenseItProcessedSeriesVariable varData(SenseItOperations ops, String varId) {
		processData(ops);
		return processData.values.get(varId);
	}

	private void processData(SenseItOperations ops) {
		if (processData == null) {
			processData = ops.process(this.data, (SenseItActivity) this.dataStore);
		}
	}
	
}
