package com.lge.data;

import com.lge.fp.List;

import java.util.Map;

/**
 * Created by jooyung.han on 12/19/14.
 */
public interface Json {

    public static JsonValue<Boolean> TRUE = new JsonBoolean(true);
    public static JsonValue<Boolean> FALSE = new JsonBoolean(false);
    public static JsonNull NULL = new JsonNull();

    static public class JsonNull implements Json {
        @Override
        public String toString() {
            return "JsonNull";
        }
    }

    static public class JsonValue<T> implements Json {
        protected final T value;

        public JsonValue(T t) {
            this.value = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof JsonValue)) return false;

            JsonValue jsonValue = (JsonValue) o;

            if (!value.equals(jsonValue.value)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    static public class JsonObject extends JsonValue<Map<String, Json>> {
        public JsonObject(Map<String, Json> jsonMap) {
            super(jsonMap);
        }

        @Override
        public String toString() {
            return "JsonObject{" + value + "}";
        }
    }

    static public class JsonArray extends JsonValue<List<? extends Json>> {
        public JsonArray(List<? extends Json> jsonList) {
            super(jsonList);
        }
    }


    static public class JsonBoolean extends JsonValue<Boolean> {
        public JsonBoolean(Boolean aBoolean) {
            super(aBoolean);
        }
    }

    static public class JsonString extends JsonValue<String> {
        public JsonString(String s) {
            super(s);
        }

        @Override
        public String toString() {
            return "JsonString{" + value + "}";
        }
    }

    static public class JsonNumber extends JsonValue<Double> {
        public JsonNumber(Double aDouble) {
            super(aDouble);
        }
    }
}
