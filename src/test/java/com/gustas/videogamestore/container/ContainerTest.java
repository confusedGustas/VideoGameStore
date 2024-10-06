package com.gustas.videogamestore.container;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {TestConfiguration.class})
class ContainerTest extends AbstractIntegrationTest {

    @Test
    void containerIsAlive() {
        assertThat(postgresqlContainer.isCreated()).isTrue();
        assertThat(postgresqlContainer.isRunning()).isTrue();
    }

}