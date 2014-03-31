package org.greengin.senseitweb.entities.users;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PermissionType {
    BROWSE,
    PROJECT_VIEW_IMAGE,
    PROJECT_BROWSE,
    CREATE_PROJECT,
    PROJECT_JOIN,
	PROJECT_MEMBER_ACTION,
	PROJECT_ADMIN,
    PROJECT_EDITION;
}
