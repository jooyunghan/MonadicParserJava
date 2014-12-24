package com.lge.fp;

import org.junit.Test;

import static com.lge.fp.Maybe.*;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MaybeTest {
    static class Base { }

    static class A extends Base { }

    static class B extends A { }

    static class C extends A { }

    @Test
    public void getOrElseAsStatic() {
        A a1 = getOrElse(some(new B()), () -> new C());
        assertThat(a1, is(instanceOf(B.class)));
        A a2 = getOrElse(none(), () -> new C());
        assertThat(a2, is(instanceOf(C.class)));
    }
    @Test
    public void orElseAsStatic() {
        Maybe<A> ma1 = orElse(some(new B()), () -> some(new C()));
        assertThat(ma1.get(), is(instanceOf(B.class)));
        Maybe<A> ma2 = orElse(none(), () -> some(new C()));
        assertThat(ma2.get(), is(instanceOf(C.class)));
    }
}