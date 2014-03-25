package org.greengin.senseitweb.entities.activities.senseit;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.data.AbstractDataProjectItem;


@Entity
public class SenseItAnalysis extends AbstractDataProjectItem {
	
	
	@Lob
	@Basic
    @Getter
    @Setter
    String text;
	
		
}
