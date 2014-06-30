package org.greengin.nquireit.logic.forum;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.rating.ForumNode;
import org.greengin.nquireit.entities.rating.ForumThread;

/**
 * Created by evilfer on 6/26/14.
 */
public class ForumRequest {
    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    String text;


    public void update(ForumNode node) {
        node.setTitle(title);
        node.setDescription(text);
    }

    public void update(ForumThread thread) {
        thread.setTitle(title);
    }

}
