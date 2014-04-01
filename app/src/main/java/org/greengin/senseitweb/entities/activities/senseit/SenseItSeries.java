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
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
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
	
	transient SenseItProcessedSeries processData;


	public HashMap<String, TimeValue> getVarValue() {
		processData();
		return SenseItOperations.tableVariables(processData, (SenseItActivity) this.dataStore);
	}
	
	public SenseItProcessedSeriesVariable varData(String varId) {
		processData();
		return processData.values.get(varId);
	}

	private void processData() {
		if (processData == null) {
			processData = SenseItOperations.instance().process(this.data, (SenseItActivity) this.dataStore);
		}
	}
	
}
