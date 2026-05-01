package com.almeja.pel.portal.integration;

import com.almeja.pel.portal.config.PostgresTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@QuarkusTestResource(PostgresTestResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {

    @Inject
    Flyway flyway;

    @BeforeAll
    void setupDatabase() {
        flyway.clean();
        flyway.migrate();
    }

}
