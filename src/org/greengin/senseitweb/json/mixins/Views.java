package org.greengin.senseitweb.json.mixins;

public class Views {
	
	public static interface User {}
	public static interface UserOpenIds extends User {};
	
	public static interface Votable extends User {}
	public static interface VotableCount extends Votable {}
	public static interface VotableNamedVotes extends Votable {}
	public static interface VotableCountModeration extends VotableCount {} 
}
