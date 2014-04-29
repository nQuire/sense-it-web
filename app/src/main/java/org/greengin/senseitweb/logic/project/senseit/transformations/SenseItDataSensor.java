package org.greengin.senseitweb.logic.project.senseit.transformations;

import lombok.Getter;
import lombok.Setter;

import java.util.Vector;

public class SenseItDataSensor {

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    String output;

    @Getter
    @Setter
    Vector<String> labels;

    @Getter
    @Setter
    SenseItDataUnits units;


    public SenseItDataSensor() {
		this.name = null;
		this.output = null;
		this.labels = new Vector<String>();
		this.units = new SenseItDataUnits();
	}
	
}