package org.digimad.builder;

import java.io.IOException;

@FunctionalInterface
public interface IOThrowFunction<T, R> {
    R apply(T arg) throws IOException;
}
