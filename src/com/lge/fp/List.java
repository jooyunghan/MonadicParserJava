package com.lge.fp;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * Created by jooyung.han on 12/19/14.
 */
public abstract class List<T> {
    private static final List<Object> NIL = new Nil();

    public static <T> List<T> cons(T h, List<T> t) {
        return new Cons<T>(h, t);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> nil() {
        return (List<T>) NIL;
    }

    @SafeVarargs
    public static <T> List<T> list(T... values) {
        List<T> result = nil();
        for (int i = values.length - 1; i >= 0; i--) {
            result = cons(values[i], result);
        }
        return result;
    }

    public static <K, V> Map<K, V> toMap(List<Pair<K, V>> pairs) {
        Map<K, V> properties = new TreeMap<>();
        pairs.forEach(pair -> properties.put(pair._1, pair._2));
        return properties;
    }

    public void forEach(Consumer<T> consumer) {
        List<T> cur = this;
        while (!cur.isEmpty()) {
            consumer.accept(cur.head());
            cur = cur.tail();
        }
    }

    abstract public List<T> tail();

    abstract public T head();

    abstract public boolean isEmpty();

    static private class Cons<T> extends List<T> {
        private final T head;
        private final List<T> tail;

        public Cons(T h, List<T> t) {
            this.head = h;
            this.tail = t;
        }

        @Override
        public List<T> tail() {
            return tail;
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cons cons = (Cons) o;
            return head.equals(cons.head) && tail.equals(cons.tail);
        }

        @Override
        public int hashCode() {
            int result = head.hashCode();
            result = 31 * result + tail.hashCode();
            return result;
        }
    }

    static private class Nil extends List<Object> {

        @Override
        public List<Object> tail() {
            throw new NoSuchElementException();
        }

        @Override
        public Object head() {
            throw new NoSuchElementException();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }

}
