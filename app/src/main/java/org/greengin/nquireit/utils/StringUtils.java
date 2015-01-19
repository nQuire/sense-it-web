package org.greengin.nquireit.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by evilfer on 11/3/14.
 */
public class StringUtils {

    public static String ellipsis(String text, int max) {
        Pattern p = Pattern.compile(String.format("^(.{%d}[^\\s]*).*", max));
        Matcher m = p.matcher(text);

        if (m.find()) {
            String match = m.group(1);
            return match.length() < text.length() ? match + "..." : match;
        } else {
            return text;
        }
    }
}
