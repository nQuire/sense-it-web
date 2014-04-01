package org.greengin.senseitweb.json.mixins;

import com.mangofactory.jsonview.BaseView;

public class Views {
	public static interface None extends BaseView {};

	public static interface User extends None {};
	public static interface UserOpenIds extends User {};
	
	public static interface Votable extends User {}
	public static interface VotableCount extends Votable {}
	public static interface VotableNamedVotes extends Votable {}
	public static interface VotableCountModeration extends VotableCount {} 
}
