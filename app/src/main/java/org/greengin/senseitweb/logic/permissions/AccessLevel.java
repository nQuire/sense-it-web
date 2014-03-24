package org.greengin.senseitweb.logic.permissions;

public class AccessLevel {
	boolean author;
	boolean admin;
	boolean member;
		
	public AccessLevel() {
		author = false;
		admin = false;
		member = false;
	}
	
	public boolean isAuthor() {
		return author;
	}
	public void setAuthor(boolean author) {
		this.author = author;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public boolean isMember() {
		return member;
	}
	public void setMember(boolean member) {
		this.member = member;
	}
	
	public boolean hasAccess(Role level) {
		switch(level) {
		case PROJECT_MEMBER:
			return member;
		case PROJECT_ADMIN:
			return admin;
		case PROJECT_EDITOR:
			return author;
		default:
			return true;
		}
	}
	
}
