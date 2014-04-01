package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.util.HashMap;
import java.util.Map;

public class SenseItDataUnits extends HashMap<String, Integer> {

	private static final long serialVersionUID = -7037426915198723223L;
	
	
	public void apply(HashMap<String, Integer> change) {
		for (Map.Entry<String, Integer> entry : change.entrySet()) {
			int value = entry.getValue() + (this.containsKey(entry.getKey()) ? this.get(entry.getKey()) : 0); 		
			this.put(entry.getKey(), value);
		}
	}
	
}