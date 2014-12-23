package com.lge.parser;

import com.lge.data.Json;
import com.lge.fp.List;
import com.lge.fp.Pair;

import java.util.function.Supplier;

import static com.lge.data.Json.*;

/**
 * Created by jooyung.han on 12/22/14.
 */

public class JsonParser {
    private static final Parser<?> comma = Parser.tok(",");
    private static final Parser<?> colon = Parser.tok(":");
    private static final Parser<String> string_ = Parser.tok(Parser.quotedString);
    private static final Parser<Double> double_ = Parser.tok(Parser.doubleNumber);

    static public Parser<Json> json() {
        return Parser.or(jsonObject(), jsonArray(), jsonNumber(), jsonString(), jsonLiteral());
    }

    static public Parser<JsonObject> jsonObject() {
        Parser<Pair<String, Json>> keyValue = Parser.seq(string_.suffix(colon), () -> json());
        return brace(() -> keyValue.sepBy(comma)).map(List::toMap).map(JsonObject::new);
    }

    static public Parser<JsonArray> jsonArray() {
        return bracket(() -> json().sepBy(comma)).map(JsonArray::new);
    }

    static public Parser<JsonNumber> jsonNumber() {
        return double_.map(JsonNumber::new);
    }

    static public Parser<JsonString> jsonString() {
        return string_.map(JsonString::new);
    }

    static public Parser<? extends Json> jsonLiteral() {
        return Parser.or(Parser.tok("true").as(() -> Json.TRUE),
                Parser.tok("false").as(() -> Json.FALSE),
                Parser.tok("null").as(() -> Json.NULL));
    }

    private static <T> Parser<T> brace(Supplier<Parser<T>> s) {
        return Parser.enclosed(Parser.tok("{"), Parser.tok("}"), s);
    }

    private static <T> Parser<T> bracket(Supplier<Parser<T>> s) {
        return Parser.enclosed(Parser.tok("["), Parser.tok("]"), s);
    }
}