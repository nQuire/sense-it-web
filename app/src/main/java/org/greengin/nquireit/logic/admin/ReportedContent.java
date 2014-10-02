package org.greengin.nquireit.logic.admin;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.users.UserProfile;

import java.util.Vector;

/**
 * Created by evilfer on 10/2/14.
 */
public class ReportedContent {

    @Getter
    @Setter
    Long id;

    @Getter
    @Setter
    long count;

    @Getter
    @Setter
    String path;

    @Getter
    @Setter
    UserProfile author;

    @Getter
    Vector<TextBlock> info = new Vector<TextBlock>();

    public void addInfo(String title, String content) {
        this.info.add(new TextBlock(title, content));
    }

    public static class TextBlock {
        @Getter
        String title;

        @Getter
        String content;

        public TextBlock(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
