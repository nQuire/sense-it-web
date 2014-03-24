package org.greengin.senseitweb.entities.activities.senseit;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.greengin.senseitweb.entities.data.AbstractDataProjectItem;


@Entity
public class SenseItAnalysis extends AbstractDataProjectItem {
	
	
	@Lob
	@Basic
	String text;
	
		
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
