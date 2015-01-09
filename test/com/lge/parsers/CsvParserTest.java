package com.lge.parsers;

import com.lge.fp.List;
import org.junit.Test;

import static com.lge.fp.List.list;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CsvParserTest {
    @Test
    public void testPlainCsv() {
        String data = "name, age, hair\njooyung, 40, black\nhyejoung, 38, brown";
        List<List<String>> table = CsvParser.parse(data);
        assertThat(table, is(list(
                List.<String>list("name", "age", "hair"),
                List.<String>list("jooyung", "40", "black"),
                List.<String>list("hyejoung", "38", "brown"))));

    }
    @Test
    public void testQuotedStr() {
        assertThat(CsvParser.field.parse("\"jooyung, han\""), is("jooyung, han"));
        
    }
    @Test
    public void testIncludingQuotedField() {
        String data = "name, age, hair\n\"jooyung, han\", 40, black\nhyejoung, 38, brown";
        List<List<String>> table = CsvParser.parse(data);
        assertThat(table, is(list(
                List.<String>list("name", "age", "hair"),
                List.<String>list("jooyung, han", "40", "black"),
                List.<String>list("hyejoung", "38", "brown"))));

    }

}