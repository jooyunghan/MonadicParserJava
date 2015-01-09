package com.lge.parsers;

import com.lge.fp.List;
import com.lge.parser.Parser;

import static com.lge.parser.Parser.*;

/**
 * Created by jooyung.han on 1/9/15.
 */
public class CsvParser {
    // token definitions
    static Parser<String> str = regex("[^\"\\r\\n,]*").map(String::trim);
    static Parser<String> quotedStr = regex("\"(\"\"|[^\"])*\"").map(CsvParser::unquote);
    static Parser<?> newLine = regex("\\r?\\n");

    // grammar
    static Parser<String> field = or(quotedStr, str);
    static Parser<List<String>> row = field.sepBy(ch(','));
    static Parser<List<List<String>>> parser = row.sepBy(newLine);

    // public api for parsing CSV content
    public static List<List<String>> parse(String input) {
        return parser.parse(input);
    }

    static String unquote(String s) {
        return s.substring(1, s.length() - 1).replaceAll("\"\"", "\"");
    }
}
