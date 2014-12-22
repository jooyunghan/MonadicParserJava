package com.lge.fp;


/**
 * Created by jooyung.han on 12/19/14.
 */
public class Strings {
    public static String fromChars(List<Character> it) {
        StringBuilder sb = new StringBuilder();
        it.forEach(sb::append);
        return sb.toString();
    }

}
