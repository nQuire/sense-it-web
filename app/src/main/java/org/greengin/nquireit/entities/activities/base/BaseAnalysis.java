package org.greengin.nquireit.entities.activities.base;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.data.AbstractDataProjectItem;


@Entity
public class BaseAnalysis extends AbstractDataProjectItem {
	
	
	@Lob
	@Basic
    @Getter
    @Setter
    String text;
	
		
}
