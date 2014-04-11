package org.greengin.senseitweb.logic.users;

import org.greengin.senseitweb.entities.users.RoleType;

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
	
	public boolean hasAccess(RoleType level) {
		switch(level) {
		case MEMBER:
			return member;
		case ADMIN:
			return admin;
		case AUTHOR:
			return author;
		default:
			return true;
		}
	}
	
}
