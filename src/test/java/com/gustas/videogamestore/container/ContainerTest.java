package com.gustas.videogamestore.container;

import com.gustas.videogamestore.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ContainerTest extends AbstractIntegrationTest {

    @Test
    void containerIsAlive() {
        assertThat(postgresqlContainer.isCreated()).isTrue();
        assertThat(postgresqlContainer.isRunning()).isTrue();
    }

}