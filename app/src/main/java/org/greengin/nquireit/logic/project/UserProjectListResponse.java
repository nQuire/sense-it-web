package org.greengin.nquireit.logic.project;

import lombok.Getter;

import java.util.Vector;

/**
 * Created by evilfer on 6/18/14.
 */
public class UserProjectListResponse {
    @Getter
    Vector<MyProjectResponse> admin;

    @Getter
    Vector<MyProjectResponse> member;

    public UserProjectListResponse() {
        this(true, true);
    }

    public UserProjectListResponse(boolean admin, boolean member) {
        this.admin = admin ? new Vector<MyProjectResponse>() : null;
        this.member = member ? new Vector<MyProjectResponse>() : null;
    }
}
