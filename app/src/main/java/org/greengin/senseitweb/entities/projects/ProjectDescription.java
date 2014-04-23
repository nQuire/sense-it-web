package org.greengin.senseitweb.entities.projects;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by evilfer on 4/23/14.
 */
public class ProjectDescription implements Serializable {

    @Getter
    @Setter
    String teaser;

    @Getter
    @Setter
    String image;

    @Getter
    @Setter
    Vector<HashMap<String, String>> blocks = new Vector<HashMap<String, String>>();
}
