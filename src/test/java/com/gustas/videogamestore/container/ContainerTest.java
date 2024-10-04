package com.gustas.videogamestore.container;

import com.gustas.videogamestore.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ContainerTest extends AbstractIntegrationTest {

    @Test
    public void containerIsAlive() {
        assertThat(postgresqlContainer.isCreated()).isTrue();
        assertThat(postgresqlContainer.isRunning()).isTrue();
    }

}