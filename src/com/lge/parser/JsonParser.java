package com.lge.parser;

import com.lge.data.Json;
import com.lge.fp.List;
import com.lge.fp.Pair;

import static com.lge.data.Json.*;

/**
 * Created by jooyung.han on 12/22/14.
 */

public class JsonParser {

    private static final Parser<?> openBrace = Parser.tok("{");
    private static final Parser<?> closeBrace = Parser.tok("}");
    private static final Parser<?> openBracket = Parser.tok("[");
    private static final Parser<?> closeBracket = Parser.tok("]");
    private static final Parser<?> comma = Parser.tok(",");
    private static final Parser<?> colon = Parser.tok(":");

    static public Parser<? extends Json> json() {
        return jsonObject()
                .or(jsonArray())
                .or(jsonNumber())
                .or(jsonString())
                .or(jsonLiteral());
    }

    static public Parser<JsonObject> jsonObject() {
        Parser<Pair<String, Json>> keyValue = Parser.seq(Parser.tok(Parser.quotedString).suffix(colon), () -> json());
        return Parser.enclosed(openBrace, closeBrace, () -> keyValue.sepBy(comma)).map(List::toMap).map(JsonObject::new);
    }

    static public Parser<JsonArray> jsonArray() {
        return Parser.enclosed(openBracket, closeBracket, () -> json().sepBy(comma)).map(JsonArray::new);
    }

    static public Parser<JsonNumber> jsonNumber() {
        return Parser.tok(Parser.doubleNumber).map(JsonNumber::new);
    }

    static public Parser<JsonString> jsonString() {
        return Parser.tok(Parser.quotedString).map(JsonString::new);
    }

    static public Parser<? extends Json> jsonLiteral() {
        return Parser.tok("true").as(() -> Json.TRUE)
                .or(Parser.tok("false").as(() -> Json.FALSE))
                .or(Parser.tok("null").as(() -> Json.NULL));
    }

}