package com.lge.parser;

import org.junit.Test;

import static com.lge.fp.Maybe.none;
import static com.lge.fp.Maybe.some;
import static com.lge.fp.Pair.pair;
import static com.lge.parser.Parser.*;
import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void parseItem() {
        assertEquals(some(pair('a', "bc")), item.apply("abc"));
        assertEquals(none(), item.apply(""));
    }

    @Test
    public void parseDigit() {
        assertEquals(some(pair('1', "bc")), digit.apply("1bc"));
        assertEquals(none(), digit.apply("abc"));
    }

    @Test
    public void parseLetter() {
        assertEquals(some(pair('a', "bc")), letter.apply("abc"));
        assertEquals(none(), letter.apply("1bc"));
    }

    @Test
    public void parseChar() {
        assertEquals(some(pair('a', "bc")), ch('a').apply("abc"));
        assertEquals(none(), ch('a').apply("bbc"));
    }

    @Test
    public void parseString() {
        assertEquals(some(pair("abc", "")), str("abc").apply("abc"));
        assertEquals(none(), str("abc").apply("ab"));
    }

    @Test
    public void parseWord() {
        assertEquals(some(pair("abc", "123")), word.apply("abc123"));
        assertEquals(some(pair("", "123")), word.apply("123"));
        assertEquals(some(pair("", "")), word.apply(""));
    }

}