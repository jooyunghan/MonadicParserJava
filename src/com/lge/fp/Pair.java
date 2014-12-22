package com.lge.fp;

public class Pair<S, T> {
    public final S _1;
    public final T _2;

    public Pair(S s, T t) {
        _1 = s;
        _2 = t;
    }

    public static <S, T> Pair<S, T> pair(S s, T t) {
        return new Pair<>(s, t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (!_1.equals(pair._1)) return false;
        if (!_2.equals(pair._2)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _1.hashCode();
        result = 31 * result + _2.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Pair{" + _1 + ", " + _2 + '}';
    }
}
