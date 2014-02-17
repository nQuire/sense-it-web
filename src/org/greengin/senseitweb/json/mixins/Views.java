package org.greengin.senseitweb.json.mixins;

public class Views {
	
	public static class User {}
	public static class UserOpenIds extends User {};
	
	public static class Votable {}
	public static class VotableIncludeAnonymousVotes extends Votable {}
	public static class VotableIncludeNamedVotes extends Votable {}
}
