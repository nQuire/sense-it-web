package org.greengin.senseitweb.json.mixins;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.senseitweb.entities.subscriptions.Subscription;

public abstract class ProjectMixIn {
	@JsonIgnore abstract Collection<Subscription> getSubscriptions();
}
