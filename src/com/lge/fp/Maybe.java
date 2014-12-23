package com.lge.fp;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by jooyung.han on 12/19/14.
 */
public interface Maybe<T> {
    public static final Maybe<Object> NONE = new None();

    static <T> Maybe<T> some(T t) {
        return new Some<T>(t);
    }

    static <T> Maybe<T> none() {
        return (Maybe<T>) NONE;
    }

    default <R> Maybe<R> map(Function<T, R> f) {
        if (isEmpty()) return none();
        else return some(f.apply(get()));
    }

    default <R> Maybe<R> flatMap(Function<T, Maybe<R>> f) {
        if (isEmpty()) return none();
        else return f.apply(get());
    }

    boolean isEmpty();

    T get();

    default T getOrElse(Supplier<T> s) {
        if (isEmpty()) return s.get();
        else return get();
    }

    default <R> Maybe<R> orElse(Supplier<Maybe<? extends R>> s) {
        if (isEmpty()) return (Maybe<R>) s.get();
        else return (Maybe<R>) this;
    }

    public static class Some<T> implements Maybe<T> {
        public final T value;

        public Some(T t) {
            this.value = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Some some = (Some) o;
            return value.equals(some.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public String toString() {
            return "Some{" +
                    "value=" + value +
                    '}';
        }
    }

    public static class None implements Maybe<Object> {

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public Object get() {
            throw new NoSuchElementException("None.get()");
        }

        @Override
        public String toString() {
            return "None{}";
        }
    }
}
