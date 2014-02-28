package org.greengin.senseitweb.json.mixins;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public abstract class SenseItAnalysisMixIn extends AbstractDataProjectItemMixIn {
	
	@JsonDeserialize(using=AnythingToStringDeserializer.class)
	@JsonSerialize(using=AnythingFromStringDeserializer.class) 
	public abstract String getTx();  
	
	
}
