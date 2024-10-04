package com.gustas.videogamestore;

import com.gustas.videogamestore.domain.Role;
import com.gustas.videogamestore.domain.User;
import com.gustas.videogamestore.repository.UserRepository;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
@DirtiesContext
public abstract class AbstractIntegrationTest {

    @Autowired
    private SpringLiquibase liquibase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    protected Authentication authentication;
    protected User user = new User();

    @Container
    protected static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:16.0")
            .withDatabaseName("video_game_store")
            .withUsername("postgres")
            .withPassword("admin");

    static {
        postgresqlContainer.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @BeforeAll
    public static void startContainerAndCreateSchema() {
        try (Connection conn = DriverManager.getConnection(postgresqlContainer.getJdbcUrl(), postgresqlContainer.getUsername(), postgresqlContainer.getPassword());
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP SCHEMA IF EXISTS public CASCADE;");
            stmt.execute("CREATE SCHEMA public;");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    @AfterAll
    public static void stopContainer() {
        postgresqlContainer.stop();
    }

    @BeforeEach
    public void init() throws LiquibaseException {
        liquibase.afterPropertiesSet();
        createUser();
        authenticateUser();
    }

    private void createUser() {
        user.setId(1L);
        user.setEmail("testUser@gmail.com");
        user.setUsername("Test");
        user.setRole(Role.USER);
        user.setEnabled(true);
        user.setPassword(bCryptPasswordEncoder.encode("Test123!"));
        userRepository.save(user);
    }

    private void authenticateUser() {
        authentication = new UsernamePasswordAuthenticationToken("1", "Test123!");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
