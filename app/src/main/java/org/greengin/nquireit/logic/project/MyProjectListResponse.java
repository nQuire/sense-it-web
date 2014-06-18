package org.greengin.nquireit.logic.project;

import lombok.Getter;

import java.util.Vector;

/**
 * Created by evilfer on 6/18/14.
 */
public class MyProjectListResponse {
    @Getter
    Vector<MyProjectResponse> admin;

    @Getter
    Vector<MyProjectResponse> member;

    public MyProjectListResponse() {
        admin = new Vector<MyProjectResponse>();
        member = new Vector<MyProjectResponse>();
    }

}
