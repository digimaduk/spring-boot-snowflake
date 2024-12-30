package org.digimad.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Data
public class Schema {
    private String name;
    private String table;
    private List<Field> fields;
    private List<Condition> conditions;

    public static Schema from(final InputStream inputStream) throws IOException {
        return new ObjectMapper().readValue(inputStream, Schema.class);
    }
}
