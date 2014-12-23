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
 * Monadic Parser.
 * <p>
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
        return spaces.flatMap(x -> s);
    }

    public static <S, T> Parser<Pair<S, T>> seq(Parser<S> s, Supplier<Parser<T>> t) {
        return s.flatMap(v1 -> t.get().map(v2 -> pair(v1, v2)));
    }

    public static <T> Parser<T> enclosed(Parser<?> left, Parser<?> right, Supplier<Parser<T>> p) {
        return seq(left, p).suffix(right).map(Pair::snd);
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <R> Parser<R> or(Parser<? extends R>... ps) {
        return input -> {
            for (Parser<? extends R> p : ps) {
                Maybe<? extends Pair<? extends R, String>> result = p.apply(input);
                if (!result.isEmpty()) return (Maybe) result;
            }
            return none();
        };
    }

    // helper
    default public T parse(String input) {
        return apply(input).get()._1;
    }

    default <R> Parser<R> flatMap(Function<T, Parser<R>> f) {
        return input -> apply(input).flatMap(p -> f.apply(p._1).apply(p._2));
    }

    default <R> Parser<R> map(Function<T, R> f) {
        return flatMap(t -> succeed(f.apply(t)));
    }

    default <R> Parser<R> as(Supplier<R> defaultValue) {
        return map(t -> defaultValue.get());
    }

    default Parser<List<T>> many1() {
        return flatMap(t -> many().map(ts -> cons(t, ts)));
    }

    default Parser<List<T>> many() {
        return or(many1(), succeed(nil()));
    }

    default Parser<T> prefix(Parser<?> left) {
        return left.flatMap(x -> this);
    }

    default Parser<T> suffix(Parser<?> right) {
        return flatMap(t -> right.map(x -> t));
    }

    default Parser<List<T>> sepBy1(Parser<?> sep) {
        return flatMap(t -> prefix(sep).many().map(ts -> cons(t, ts)));
    }

    default Parser<List<T>> sepBy(Parser<?> sep) {
        return or(sepBy1(sep), succeed(nil()));
    }
}