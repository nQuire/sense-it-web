package org.greengin.nquireit.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.nquireit.utils.TimeValue;

import java.util.HashMap;
import java.util.Vector;

public abstract class SenseItSeriesMixIn extends AbstractDataProjectItemMixIn {
	@JsonIgnore
    abstract HashMap<Long, Vector<TimeValue>> getData();



}
