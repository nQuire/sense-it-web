package org.greengin.nquireit.json;

import com.mangofactory.jsonview.BaseView;

public class Views {
	public static interface None extends BaseView {};

    public static interface User extends None {};
    public static interface UserName extends User {};
	public static interface UserProfileData extends UserName {};
	
	public static interface Votable extends UserName {}
	public static interface VotableCount extends Votable {}
	public static interface VotableNamedVotes extends Votable {}
	public static interface VotableCountModeration extends VotableCount {}

    public static interface ForumList extends UserName {};
    public static interface ForumNode extends UserName {};
    public static interface ForumThread extends VotableCount {};
}
