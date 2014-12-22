package com.lge.parser;

import com.lge.data.Json;
import org.junit.Test;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static com.lge.fp.List.list;
import static com.lge.fp.List.toMap;
import static com.lge.parser.JsonParser.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by jooyung.han on 12/22/14.
 */
public class JsonParserTest {

    @Test(expected = NoSuchElementException.class)
    public void testFailures() {
        jsonNumber().parse("abc12.0abc");
    }

    @Test
    public void testJsonNumber() {
        assertEquals(new Json.JsonString("abc"), jsonString().parse("\"abc\""));
        assertEquals(new Json.JsonNumber(12.0), jsonNumber().parse("12.0abc"));
        assertEquals(new Json.JsonNumber(12.0), jsonNumber().parse("12.0eabc"));
        assertEquals(new Json.JsonNumber(-12.0e+2), jsonNumber().parse("-12.0e+2abc"));
    }

    @Test
    public void testJsonObject() {
        Map<String, Json> props = new TreeMap<>();
        props.put("p1", new Json.JsonString("string"));
        props.put("p2", new Json.JsonArray(list(Json.TRUE, Json.FALSE, Json.NULL, new Json.JsonObject(toMap(list())))));
        assertEquals(new Json.JsonObject(props), json().parse("{" +
                "\"p1\" : \"string\"," +
                "\"p2\" : [true, false, null, {}]" +
                "}end"));
    }

}
