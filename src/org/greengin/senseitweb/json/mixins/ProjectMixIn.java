package org.greengin.senseitweb.json.mixins;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.greengin.senseitweb.entities.subscriptions.Subscription;

public abstract class ProjectMixIn {
	@JsonIgnore abstract Collection<Subscription> getSubscriptions();
}
