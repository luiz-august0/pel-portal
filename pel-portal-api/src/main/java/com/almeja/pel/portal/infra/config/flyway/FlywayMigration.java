package com.almeja.pel.portal.infra.config.flyway;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class FlywayMigration {

    private final DataSource dataSource;

    private static final String SCHEMA = "public";

    private static final String MIGRATION_CLASSPATH = "classpath:db/migration";

    @PostConstruct
    public void doMigrations() {
        configure().repair();
        configure().migrate();
    }

    private Flyway configure() {
        return Flyway.configure()
                .dataSource(dataSource)
                .schemas(FlywayMigration.SCHEMA)
                .locations(MIGRATION_CLASSPATH)
                .load();
    }

}