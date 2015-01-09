package com.lge.fp;

import org.junit.Test;

import static com.lge.fp.List.list;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.*;

public class ListTest {

    @Test
    public void testToString() throws Exception {
        assertThat(list(), hasToString("[]"));
        assertThat(list(1,2,3), hasToString("[1, 2, 3]"));
    }
}