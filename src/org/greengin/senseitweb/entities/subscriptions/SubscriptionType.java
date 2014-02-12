package org.greengin.senseitweb.entities.subscriptions;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;



public enum SubscriptionType {
	AUTHOR,
	ADMIN,
	MEMBER;
	
	@JsonValue
    public String getValue() { return this.name().toLowerCase(); }

    @JsonCreator
    public static SubscriptionType create(String val) {
    	SubscriptionType[] units = SubscriptionType.values();
        for (SubscriptionType unit : units) {
            if (unit.getValue().equals(val)) {
                return unit;
            }
        }
        return MEMBER;
    }
}
