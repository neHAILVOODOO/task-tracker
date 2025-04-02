package com.example.tasktracker.repo;

import com.example.tasktracker.model.entity.Comment;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Тест репозитория CommentRepo")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class CommentRepoTest {


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
    @Autowired
    private CommentRepo commentRepo;

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
    @DisplayName("[create] Должен успешно создать комментарий")
    public void createCommentTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);
        taskRepo.save(task);
        Comment comment = new Comment(0, "Купил все продукты, кроме перчиков чили. Их не было.", user, task);
        comment = commentRepo.save(comment);

        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isPositive();
        assertThat(comment.getText()).isEqualTo("Купил все продукты, кроме перчиков чили. Их не было.");

    }

    @Test
    @DisplayName("[create] Не должен создать комментарий с null текстом")
    public void createCommentWithNullNameTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);
        taskRepo.save(task);
        Comment comment = new Comment(0, null, user, task);

        assertThatThrownBy(() -> commentRepo.save(comment))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("[create] Не должен создать комментарий с null автором")
    public void createCommentWithNullAuthorTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);
        taskRepo.save(task);
        Comment comment = new Comment(0, "Купил все продукты, кроме перчиков чили. Их не было.", null, task);

        assertThatThrownBy(() -> commentRepo.save(comment))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("[create] Не должен создать комментарий с null задачей")
    public void createCommentWithNullTaskTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);
        taskRepo.save(task);
        Comment comment = new Comment(0, "Купил все продукты, кроме перчиков чили. Их не было.", user, null);

        assertThatThrownBy(() -> commentRepo.save(comment))
                .isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    @DisplayName("[select] Должен найти все комментарии по задаче")
    public void findAllCommentByTaskTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);
        taskRepo.save(task);
        Comment comment = new Comment(0, "Купил все продукты, кроме перчиков чили. Их не было.", user, task);
        commentRepo.save(comment);


        List<Comment> comments = commentRepo.findAllByTask(task);
        assertThat(comments).isNotNull();
        assertThat(comments.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("[select] Не должен найти комментарии по null задаче")
    public void findAllCommentByNullTaskTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);
        task = taskRepo.save(task);
        Comment comment = new Comment(0, "Купил все продукты, кроме перчиков чили. Их не было.", user, task);
        commentRepo.save(comment);

        List<Comment> result = commentRepo.findAllByTask(null);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("[select] Должен найти все комментарии по id и задаче")
    public void findByIdAndTaskTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);
        task = taskRepo.save(task);
        Comment comment = new Comment(0, "Купил все продукты, кроме перчиков чили. Их не было.", user, task);
        commentRepo.save(comment);

        Optional<Comment> foundComment = commentRepo.findByIdAndTask(1, task);

        assertThat(foundComment)
                .isPresent()
                .hasValueSatisfying(found -> {
                    assertThat(found.getId()).isPositive();
                    assertThat(found.getId()).isEqualTo(1);
                    assertThat(found.getText()).isEqualTo("Купил все продукты, кроме перчиков чили. Их не было.");

                });

    }

    @Test
    @DisplayName("[select] Не должен найти комментарии по несуществующему id, но существующей задаче")
    public void findByNotExistingIdAndTaskTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);
        task = taskRepo.save(task);
        Comment comment = new Comment(0, "Купил все продукты, кроме перчиков чили. Их не было.", user, task);
        commentRepo.save(comment);

        assertThat(commentRepo.findByIdAndTask(-111, task)).isEmpty();

    }

    @Test
    @DisplayName("[select] Не должен найти комментарии по существующему id, но несуществующей задаче")
    public void findByIdAndNullTaskTest() {
        User user = new User(0, "jimmy", "jimmy@ya.com", "123", 18, UserRole.EMPLOYEE, null, null, null);
        userRepo.save(user);
        Task task = new Task(0, "Купить продукты", "Купить продукты в магазине на ближайшую неделю", TaskPriority.MEDIUM, TaskStatus.WAITING, false, user, user, null);
        task = taskRepo.save(task);
        Comment comment = new Comment(0, "Купил все продукты, кроме перчиков чили. Их не было.", user, task);
        commentRepo.save(comment);

        assertThat(commentRepo.findByIdAndTask(1, null)).isEmpty();

    }

}
