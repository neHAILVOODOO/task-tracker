package com.example.tasktracker.repo;

import com.example.tasktracker.model.entity.User;
import com.example.tasktracker.model.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.bind.annotation.RequestBody;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Тест репозитория UserRepo")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class UserRepoTest {

    private final static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17.0")
            .withDatabaseName("testdb")
            .withUsername("junit")
            .withPassword("password");

    @DynamicPropertySource
    private static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private UserRepo userRepo;

    @BeforeAll
    static void beforeAll() {
        postgresContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgresContainer.stop();
    }

    @BeforeEach
    public void setUp() {

        String url = postgresContainer.getJdbcUrl();
        String user = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("TRUNCATE TABLE users RESTART IDENTITY CASCADE");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    @DisplayName("[create] Должен успешно создать пользователя")
    public void createUserTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        user = userRepo.save(user);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isPositive();
        assertThat(user.getName()).isEqualTo("jimmy");
        assertThat(user.getEmail()).isEqualTo("jimmy@ya.com");

    }

    @Test
    @DisplayName("[create] Не должен создать пользователя с пустым именем")
    public void createUserWithNullNameShouldThrowExceptionTest() {
        User user = new User(0, null, "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);

        assertThatThrownBy(() -> userRepo.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("[create] Не должен создать пользователя с пустым email")
    public void createUserWithNullEmailShouldThrowExceptionTest() {
        User user = new User(0, "jimmy", null, "123", 18, UserRole.EMPLOYEE, null, null, null);

        assertThatThrownBy(() -> userRepo.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("[create] Не должен создать пользователя с пустым паролем")
    public void createUserWithNullPasswordShouldThrowExceptionTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", null, 18, UserRole.EMPLOYEE, null, null, null);

        assertThatThrownBy(() -> userRepo.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("[create] Не должен создать пользователя без роли")
    public void createUserWithNullRoleShouldThrowExceptionTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, null, null, null, null);

        assertThatThrownBy(() -> userRepo.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("[select] Должен найти пользователя по id")
    public void findUserByIdTest() {

        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);

        userRepo.save(user);
        user = userRepo.findUserById(1);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isPositive();
        assertThat(user.getName()).isEqualTo("jimmy");
        assertThat(user.getEmail()).isEqualTo("jimmy@ya.com");
    }

    @Test
    @DisplayName("[select] Не должен найти пользователя по несуществующему id")
    public void findUserByNotExistingIdTest() {

        assertThat(userRepo.findUserById(-1)).isNull();
    }

    @Test
    @DisplayName("[select] Должен найти пользователя по email")
    public void findUserByEmailTest() {

        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        userRepo.save(user);
        User foundUser = userRepo.findUserByEmail("jimmy@ya.com");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isPositive();
        assertThat(foundUser.getName()).isEqualTo("jimmy");
        assertThat(foundUser.getEmail()).isEqualTo("jimmy@ya.com");
    }

    @Test
    @DisplayName("[select] Не должен найти пользователя по null email")
    public void findUserByNullEmailTest() {

        assertThat(userRepo.findUserByEmail(null)).isNull();
    }

    @Test
    @DisplayName("[select] Должен проверить, существует ли пользователь по email")
    public void isUserExistsByEmailTest() {

        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        userRepo.save(user);
        boolean exists = userRepo.existsByEmail(user.getEmail());


        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("[select] Должен проверить, существует ли пользователь по null email")
    public void isUserExistsByNullEmailTest() {

        boolean exists = userRepo.existsByEmail(null);
        assertThat(exists).isFalse();
    }

}
