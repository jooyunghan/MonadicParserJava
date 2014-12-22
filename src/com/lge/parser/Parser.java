package com.lge.parser;

import com.lge.fp.List;
import com.lge.fp.Maybe;
import com.lge.fp.Pair;
import com.lge.fp.Strings;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lge.fp.List.cons;
import static com.lge.fp.List.nil;
import static com.lge.fp.Maybe.none;
import static com.lge.fp.Maybe.some;
import static com.lge.fp.Pair.pair;

/**
 * Created by jooyung.han on 12/19/14.
 */

public interface Parser<T> extends Function<String, Maybe<Pair<T, String>>> {
    // primitives
    public static Parser<Character> item = input -> {
        if (input.isEmpty())
            return none();
        else
            return some(pair(input.charAt(0), input.substring(1)));
    };
    // default parsers
    public static Parser<Character> digit = sat(Character::isDigit);
    public static Parser<Character> letter = sat(Character::isLetter);
    public static Parser<Character> space = sat(Character::isWhitespace);
    public static Parser<String> word = letter.many().map(Strings::fromChars);
    public static Parser<String> quotedString = enclosed(ch('"'), ch('"'), () -> sat(c -> c != '"').many()).map(Strings::fromChars);
    public static Parser<Double> doubleNumber = regex("[+-]?\\d+.\\d+([eE][+-]?\\d+)?").map(Double::valueOf);
    public static Parser<String> spaces = space.many().map(Strings::fromChars);

    public static <T> Parser<T> succeed(T t) {
        return input -> some(pair(t, input));
    }

    public static <T> Parser<T> fail() {
        return input -> none();
    }

    public static Parser<Character> sat(Predicate<Character> p) {
        return item.flatMap(c -> {
            if (p.test(c)) {
                return succeed(c);
            } else {
                return fail();
            }
        });
    }

    public static Parser<Character> ch(char c) {
        return sat(ch -> ch == c);
    }

    public static Parser<String> str(String s) {
        return input -> {
            if (input.startsWith(s)) {
                return some(pair(s, input.substring(s.length())));
            } else {
                return none();
            }
        };
    }

    public static Parser<String> regex(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return input -> {
            Matcher matcher = pattern.matcher(input);
            if (matcher.lookingAt()) {
                return some(pair(matcher.group(), input.substring(matcher.end())));
            } else {
                return none();
            }
        };
    }

    public static Parser<String> tok(String s) {
        return tok(str(s));
    }

    public static <R> Parser<R> tok(Parser<R> s) {
        return s.suffix(spaces);
    }

    public static <S, T> Parser<Pair<S, T>> seq(Parser<S> s, Supplier<Parser<? extends T>> t) {
        return s.flatMap(v1 -> t.get().map(v2 -> pair(v1, v2)));
    }

    public static <T> Parser<T> enclosed(Parser<?> left, Parser<?> right, Supplier<Parser<? extends T>> self) {
        return seq(left, self).suffix(right).map(p -> p._2);
    }

    // helper
    default public T parse(String input) {
        return apply(input).get()._1;
    }

    default Parser<T> or(Parser<?> alt) {
        final Parser<T> self = this;
        return input -> self.apply(input).orElse(() -> alt.apply(input));
    }

    default <R> Parser<R> flatMap(Function<T, Parser<R>> f) {
        final Parser<T> self = this;
        return input -> {
            Maybe<Pair<T, String>> result = self.apply(input);
            return result.map(p -> f.apply(p._1).apply(p._2)).getOrElse(() -> none());
        };
    }

    default <R> Parser<R> map(Function<T, R> f) {
        final Parser<T> self = this;
        return input -> self.apply(input).map(p -> pair(f.apply(p._1), p._2));
    }

    default <R> Parser<R> as(Supplier<R> defaultValue) {
        return map(t -> defaultValue.get());
    }

    default Parser<List<T>> many1() {
        final Parser<T> self = this;
        return flatMap(t -> self.many().map(ts -> cons(t, ts)));
    }

    default Parser<List<T>> many() {
        return many1().or(succeed(nil()));
    }

    default Parser<T> prefix(Parser<?> left) {
        final Parser<T> self = this;
        return left.flatMap(x -> self);
    }

    default Parser<T> suffix(Parser<?> right) {
        return flatMap(t -> right.flatMap(x -> succeed(t)));
    }

    default Parser<List<T>> sepBy1(Parser<?> sep) {
        final Parser<T> self = this;
        return self.flatMap(t -> self.prefix(sep).many().map(ts -> cons(t, ts)));
    }

    default Parser<List<T>> sepBy(Parser<?> sep) {
        return sepBy1(sep).or(succeed(nil()));
    }
}