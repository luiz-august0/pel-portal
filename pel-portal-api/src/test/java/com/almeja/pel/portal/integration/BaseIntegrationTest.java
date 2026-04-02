package com.almeja.pel.portal.integration;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {

    @Autowired
    private Flyway flyway;

    @BeforeAll
    void setupDatabase() {
        // Limpa completamente o banco de dados
        flyway.clean();

        // Executa todas as migrations para recriar o banco
        flyway.migrate();
    }

}
