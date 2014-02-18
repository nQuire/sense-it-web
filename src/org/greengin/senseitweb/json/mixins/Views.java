package org.greengin.senseitweb.json.mixins;

public class Views {
	
	public static class User {}
	public static class UserOpenIds extends User {};
	
	public static class Votable extends User {}
	public static class VotableIncludeCount extends Votable {}
	public static class VotableIncludeNamedVotes extends Votable {}
}
