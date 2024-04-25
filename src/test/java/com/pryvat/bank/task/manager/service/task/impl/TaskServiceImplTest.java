package com.pryvat.bank.task.manager.service.task.impl;

import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.entity.task.TaskEntity;
import com.pryvat.bank.task.manager.entity.task.TaskStatus;
import com.pryvat.bank.task.manager.exception.EntityNotFoundException;
import com.pryvat.bank.task.manager.exception.TaskValidationException;
import com.pryvat.bank.task.manager.exception.WrongTaskStatusException;
import com.pryvat.bank.task.manager.repository.h2.task.H2TaskRepository;
import com.pryvat.bank.task.manager.service.task.TaskService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"test"})
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskServiceImplTest {
    @Autowired
    private TaskService taskService;
    @Autowired
    private H2TaskRepository taskRepository;

    @Test
    @Order(1)
    void createTask() {
        Task task = prepareTasks().get(0);

        Long taskId = taskService.createTask(task);

        assertEquals(1L, taskId);
    }

    @Test
    void deleteTask() {
        taskService.deleteTask(100L);

        assertEquals(1, taskRepository.findAll().size());
    }

    @Test
    void getAllTasks() {
        List<Task> allTasks = taskService.getAllTasks();

        assertEquals(2, allTasks.size());
    }

    @Test
    void updateTaskStatus() {
        taskService.updateTaskStatus(100L, "DONE");

        assertEquals(TaskStatus.DONE, taskRepository.findById(100L).get().getStatus());
    }

    @Test
    void checkUpdateTaskStatusWithWrongId() {
        assertThrows(EntityNotFoundException.class,
                () -> taskService.updateTaskStatus(7L, "123"),
                "Expected EntityNotFound to be thrown");
    }

    @Test
    void checkUpdateTaskFieldsWithWrongId() {
        Task task = prepareTasks().get(0);

        assertThrows(EntityNotFoundException.class,
                () -> taskService.updateTaskFields(task),
                "Expected EntityNotFound to be thrown");
    }

    @Test
    void checkMaxTaskLimitValidation() {
        List<Task> tasks = prepareTasks();

        taskService.createTask(tasks.get(0));
        assertThrows(TaskValidationException.class,
                () -> taskService.createTask(tasks.get(1)),
                "Expected TaskValidationException to be thrown");
    }

    @Test
    void checkTaskNameAndStatusValidation() {
        Task task = Task.builder()
                .name("first")
                .status(TaskStatus.CREATED)
                .description("some description")
                .build();

        assertThrows(TaskValidationException.class,
                () -> taskService.createTask(task),
                "Expected TaskValidationException to be thrown");
    }

    @Test
    void updateTaskFields() {
        Task task = Task.builder()
                .id(100L)
                .name("first_task")
                .status(TaskStatus.IN_PROGRESS)
                .description("somedescription")
                .build();

        taskService.updateTaskFields(task);

        TaskEntity entity = taskRepository.findById(task.getId()).get();
        assertEquals(task.getName(), entity.getName());
        assertEquals(task.getStatus(), entity.getStatus());
        assertEquals(task.getDescription(), entity.getDescription());
    }

    @Test
    void checkNameSpecialCharacterValidation() {
        Task task = prepareTasks().get(0);
        task.setName("na!");

        assertThrows(ConstraintViolationException.class,
                () -> taskService.createTask(task),
                "Expected name size ConstraintViolationException to be thrown");
    }

    @Test
    void checkNameNullValidation() {
        Task task = prepareTasks().get(0);
        task.setName(null);

        assertThrows(ConstraintViolationException.class,
                () -> taskService.createTask(task),
                "Expected name size ConstraintViolationException to be thrown");
    }

    @Test
    void checkNameSizeValidation() {
        Task task = prepareTasks().get(0);
        task.setName("na");

        assertThrows(ConstraintViolationException.class,
                () -> taskService.createTask(task),
                "Expected name size ConstraintViolationException to be thrown");
    }

    @Test
    void checkDescriptionNullValidation() {
        Task task = prepareTasks().get(0);
        task.setDescription(null);

        assertThrows(ConstraintViolationException.class,
                () -> taskService.createTask(task),
                "Expected description size ConstraintViolationException to be thrown");
    }

    @Test
    void checkDescriptionSizeValidation() {
        Task task = prepareTasks().get(0);
        task.setDescription("123");

        assertThrows(ConstraintViolationException.class,
                () -> taskService.createTask(task),
                "Expected description size ConstraintViolationException to be thrown");
    }

    @Test
    void checkStatusNullValidation() {
        Task task = prepareTasks().get(0);
        task.setStatus(null);

        assertThrows(ConstraintViolationException.class,
                () -> taskService.createTask(task),
                "Expected description size ConstraintViolationException to be thrown");
    }

    @Test
    void checkWrongStatusViolation() {
        assertThrows(WrongTaskStatusException.class,
                () -> taskService.updateTaskStatus(100L, "123"),
                "Expected WrongTaskStatusException to be thrown");
    }

    private List<Task> prepareTasks() {
        Task task = Task.builder()
                .id(2L)
                .name("fourth")
                .status(TaskStatus.DONE)
                .description("description")
                .build();
        Task task1 = Task.builder()
                .id(1L)
                .name("third")
                .status(TaskStatus.CREATED)
                .description("somedesc")
                .build();
        return List.of(task1, task);
    }
}