package com.lge.parsers;

import com.lge.data.Json;
import org.junit.Test;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static com.lge.fp.List.list;
import static com.lge.fp.List.toMap;
import static com.lge.parsers.JsonParser.*;
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
        props.put("p1", new Json.JsonString("\uac00str\ning\""));
        props.put("p2", new Json.JsonArray(list(Json.TRUE, Json.FALSE, Json.NULL, new Json.JsonObject(toMap(list())))));
        assertEquals(new Json.JsonObject(props), json().parse("\n" +
                "{\n" +
                "\t\"p1\" : \"\\uac00str\\ning\\\"\",\n" +
                "\t\"p2\" : [true, false, null, {}]\n" +
                "}end"));
    }

}
