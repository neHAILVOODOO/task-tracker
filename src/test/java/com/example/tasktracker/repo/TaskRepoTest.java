package com.example.tasktracker.repo;

import com.example.tasktracker.model.entity.Task;
import com.example.tasktracker.model.entity.User;
import com.example.tasktracker.model.enums.TaskPriority;
import com.example.tasktracker.model.enums.TaskStatus;
import com.example.tasktracker.model.enums.UserRole;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Тест репозитория TaskRepo")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class TaskRepoTest {

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
    @Autowired
    private TaskRepo taskRepo;

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
    @DisplayName("[create] Должен успешно создать задачу")
    public void createTaskTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        user = userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);
        task = taskRepo.save(task);

        assertThat(task).isNotNull();
        assertThat(task.getId()).isPositive();
        assertThat(task.getName()).isEqualTo("Купить продукты");
        assertThat(task.getPriority()).isEqualTo(TaskPriority.MEDIUM);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.WAITING);
    }

    @Test
    @DisplayName("[create] Не должен создать задачу с null именем")
    public void createTaskWithNullNameTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        user = userRepo.save(user);
        Task task = new Task(0, null, "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);

        assertThatThrownBy(() -> taskRepo.save(task))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("[create] Не должен создать задачу с null описанием")
    public void createTaskWithNullDescriptionTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        user = userRepo.save(user);
        Task task = new Task(0, "Купить продукты", null, TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);

        assertThatThrownBy(() -> taskRepo.save(task))
                .isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    @DisplayName("[create] Не должен создать задачу с null приоритетом")
    public void createTaskWithNullPriorityTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        user = userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", null, TaskStatus.WAITING, false, user, user, null);

        assertThatThrownBy(() -> taskRepo.save(task))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("[create] Не должен создать задачу с null статусом")
    public void createTaskWithNullStatusTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        user = userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, null, false, user, user, null);

        assertThatThrownBy(() -> taskRepo.save(task))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("[create] Не должен создать задачу с null автором")
    public void createTaskWithNullAuthorTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        user = userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, null, user, null);

        assertThatThrownBy(() -> taskRepo.save(task))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("[create] Не должен создать задачу с null исполнителем")
    public void createTaskWithNullExecutorTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        user = userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, null, null);

        assertThatThrownBy(() -> taskRepo.save(task))
                .isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    @DisplayName("[select] Должен найти все задачи по автору")
    public void findAllByAuthorTest() {
        User user1 = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.MANAGER, null, null, null);
        user1 = userRepo.save(user1);
        User user2 = new User(0, "jimmy2", "jimmy2@ya.com", "1234", 19, UserRole.MANAGER, null, null, null);
        user2 = userRepo.save(user2);
        Task task1 = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user2, user2, null);
        taskRepo.save(task1);
        Task task2 = new Task(0, "Обновить оборудование", "Обновить оборудование на предприятии", TaskPriority.HIGH, TaskStatus.WAITING, false, user1, user1, null);
        taskRepo.save(task2);

        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());

        Page<Task> tasksByAuthor = taskRepo.findAllByAuthor(user1, pageable);

        assertThat(tasksByAuthor).isNotNull();
        assertThat(tasksByAuthor.getTotalElements()).isEqualTo(1);
        assertThat(tasksByAuthor.getTotalPages()).isEqualTo(1);
        assertThat(tasksByAuthor.getNumber()).isZero();
    }

    @Test
    @DisplayName("[select] Не должен найти задачи по null автору")
    public void findAllByNullAuthorTest() {
        User user1 = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.MANAGER, null, null, null);
        userRepo.save(user1);
        Task task1 = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user1, user1, null);
        taskRepo.save(task1);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> result = taskRepo.findAllByAuthor(null, pageable);

        assertThat(result).isNotNull();
        assertThat(result.hasContent()).isFalse();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("[select] Должен найти все задачи по исполнителю")
    public void findAllByExecutorTest() {
        User user1 = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.MANAGER, null, null, null);
        user1 = userRepo.save(user1);
        User user2 = new User(0, "jimmy2", "jimmy2@ya.com", "1234", 19, UserRole.MANAGER, null, null, null);
        user2 = userRepo.save(user2);
        Task task1 = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user2, user1, null);
        taskRepo.save(task1);
        Task task2 = new Task(0, "Обновить оборудование", "Обновить оборудование на предприятии", TaskPriority.HIGH, TaskStatus.WAITING, false, user1, user1, null);
        taskRepo.save(task2);

        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());

        Page<Task> tasksByAuthor = taskRepo.findAllByExecutor(user1, pageable);

        assertThat(tasksByAuthor).isNotNull();
        assertThat(tasksByAuthor.getTotalElements()).isEqualTo(2);
        assertThat(tasksByAuthor.getTotalPages()).isEqualTo(1);
        assertThat(tasksByAuthor.getNumber()).isZero();
    }

    @Test
    @DisplayName("[select] Не должен найти задачи по null исполнителю")
    public void findAllByNullExecutorTest() {
        User user1 = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.MANAGER, null, null, null);
        userRepo.save(user1);
        Task task1 = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user1, user1, null);
        taskRepo.save(task1);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> result = taskRepo.findAllByExecutor(null, pageable);

        assertThat(result).isNotNull();
        assertThat(result.hasContent()).isFalse();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("[select] Должен найти задачу по id")
    public void findByIdTest() {
        User user1 = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.MANAGER, null, null, null);
        user1 = userRepo.save(user1);

        Task task1 = new Task(0, "Обновить оборудование", "Обновить оборудование на предприятии", TaskPriority.HIGH, TaskStatus.WAITING, false, user1, user1, null);
        taskRepo.save(task1);

        Optional<Task> foundTask = taskRepo.findById(1);

        assertThat(foundTask)
                .isPresent()
                .hasValueSatisfying(task -> {
                    assertThat(task.getId()).isPositive();
                    assertThat(task.getName()).isEqualTo("Обновить оборудование");
                    assertThat(task.getPriority()).isEqualTo(TaskPriority.HIGH);
                    assertThat(task.getStatus()).isEqualTo(TaskStatus.WAITING);
                });
    }

    @Test
    @DisplayName("[select] Не должен найти задачу по несуществующему id")
    public void findByNotExistingIdTest() {
        User user1 = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.MANAGER, null, null, null);
        user1 = userRepo.save(user1);

        Task task1 = new Task(0, "Обновить оборудование", "Обновить оборудование на предприятии", TaskPriority.HIGH, TaskStatus.WAITING, false, user1, user1, null);
        taskRepo.save(task1);

        assertThat(taskRepo.findById(-1321)).isEmpty();
    }


    @Test
    @DisplayName("[select] Должен найти задачу по исполнителю и id")
    public void findByExecutorAndIdTest() {
        User user1 = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.MANAGER, null, null, null);
        user1 = userRepo.save(user1);

        Task task1 = new Task(0, "Обновить оборудование", "Обновить оборудование на предприятии", TaskPriority.HIGH, TaskStatus.WAITING, false, user1, user1, null);
        taskRepo.save(task1);

        Optional<Task> foundTask = taskRepo.findByExecutorAndId(user1, 1);

        assertThat(foundTask)
                .isPresent()
                .hasValueSatisfying(task -> {
                    assertThat(task.getId()).isPositive();
                    assertThat(task.getName()).isEqualTo("Обновить оборудование");
                    assertThat(task.getPriority()).isEqualTo(TaskPriority.HIGH);
                    assertThat(task.getStatus()).isEqualTo(TaskStatus.WAITING);
                });

    }

    @Test
    @DisplayName("[select] Не должен найти задачу по null исполнителю, но существующему id")
    public void findByNullExecutorAndId() {
        User user1 = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.MANAGER, null, null, null);
        user1 = userRepo.save(user1);

        Task task1 = new Task(0, "Обновить оборудование", "Обновить оборудование на предприятии", TaskPriority.HIGH, TaskStatus.WAITING, false, user1, user1, null);
        taskRepo.save(task1);

        assertThat(taskRepo.findByExecutorAndId(null, 1)).isEmpty();
    }

    @Test
    @DisplayName("[select] Не должен найти задачу по существующему исполнителю, но несуществующему id")
    public void findByExecutorAndNotExistingId() {
        User user1 = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.MANAGER, null, null, null);
        user1 = userRepo.save(user1);

        Task task1 = new Task(0, "Обновить оборудование", "Обновить оборудование на предприятии", TaskPriority.HIGH, TaskStatus.WAITING, false, user1, user1, null);
        taskRepo.save(task1);

        assertThat(taskRepo.findByExecutorAndId(user1, -111)).isEmpty();
    }


}
