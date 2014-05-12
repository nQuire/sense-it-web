package org.greengin.nquireit.entities.activities.senseit;

import java.io.Serializable;
import java.util.Vector;

public class SenseItTransformation implements Serializable {
	
	private static final long serialVersionUID = -7889194180195803542L;

	String id;
	
	String name;
	
	String type;
	
	int weight;
	
	Vector<String> inputs = new Vector<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Vector<String> getInputs() {
		return inputs;
	}

	public void setInputs(Vector<String> inputs) {
		this.inputs = inputs;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}	
}
