package org.digimad;

import lombok.extern.slf4j.Slf4j;
import org.digimad.builder.IOThrowFunction;
import org.digimad.builder.Schema;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Map<String, Schema> schemaMap() {
        log.info("Loading json files");
        try {
            final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            final Resource[] resources = resolver.getResources("mappings/*.json");
            return Arrays.stream(resources)
                .map(resource -> wrappedApply(Schema::from, resource))
                .collect(Collectors.toMap(e -> e.getName(), e -> e));
        } catch (Exception e) {
            log.warn("Unable to Load");
        }
        return null;
    }

    private static <R> R wrappedApply(final IOThrowFunction<InputStream, R> function, final Resource resource) {
        try {
            R r = function.apply(resource.getInputStream());
            log.info("Loaded Json files");
            return r;
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
