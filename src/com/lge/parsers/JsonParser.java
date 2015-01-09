package com.lge.parsers;

import com.lge.data.Json;
import com.lge.fp.List;
import com.lge.fp.Pair;
import com.lge.parser.Parser;

import java.util.function.Supplier;

import static com.lge.data.Json.*;

/**
 * Parsing expression grammar for JSON
 * 
 * value := object / array / number / string / literal
 * object := '{' (keyvalue (',' keyvalue)*)? '}'
 * keyvalue := string ':' value 
 * array := '[' (value (',' value))?  ']' 
 * literal := 'true' / 'false' / 'null'
 */

public class JsonParser {
    private static final Parser<?> comma = Parser.tok(",");
    private static final Parser<?> colon = Parser.tok(":");
    private static final Parser<String> string_ = Parser.tok(Parser.quotedString);
    private static final Parser<Double> double_ = Parser.tok(Parser.doubleNumber);

    // value := object / array / number / string / literal
    static public Parser<Json> json() {
        return Parser.or(jsonObject(), jsonArray(), jsonNumber(), jsonString(), jsonLiteral());
    }

    // object := '{' (keyvalue (',' keyvalue)*)? '}'
    // keyvalue := string ':' value
    static public Parser<JsonObject> jsonObject() {
        Parser<Pair<String, Json>> keyValue = Parser.seq(string_.suffix(colon), () -> json());
        return brace(() -> keyValue.sepBy(comma)).map(List::toMap).map(JsonObject::new);
    }

    // array := '[' (value (',' value))?  ']'
    static public Parser<JsonArray> jsonArray() {
        return bracket(() -> json().sepBy(comma)).map(JsonArray::new);
    }

    static public Parser<JsonNumber> jsonNumber() {
        return double_.map(JsonNumber::new);
    }

    static public Parser<JsonString> jsonString() {
        return string_.map(JsonString::new);
    }

    // literal := 'true' / 'false' / 'null'
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