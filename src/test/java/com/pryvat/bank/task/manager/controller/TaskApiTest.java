package com.pryvat.bank.task.manager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pryvat.bank.task.manager.entity.task.TaskEntity;
import com.pryvat.bank.task.manager.entity.task.TaskStatus;
import com.pryvat.bank.task.manager.model.Status;
import com.pryvat.bank.task.manager.model.TaskDTO;
import com.pryvat.bank.task.manager.model.TaskRequest;
import com.pryvat.bank.task.manager.repository.h2.task.H2TaskRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"test"})
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskApiTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private H2TaskRepository taskRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private final String TASK_URL = "/task";

    @Test
    @Order(1)
    void testCreateTask() throws Exception {
        MvcResult mvcResult = mvc.perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskRequest())))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertEquals(1L, id);
    }

    @Test
    void deleteTask() throws Exception {
        mvc.perform(delete(TASK_URL + "/100"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllTasks() throws Exception {
        MvcResult mvcResult = mvc.perform(get(TASK_URL))
                .andExpect(status().isOk())
                .andReturn();

        List<TaskDTO> taskDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        assertEquals(2, taskDTOS.size());
    }

    @Test
    void changeTaskFields() throws Exception {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("changed");
        taskRequest.setStatus(Status.IN_PROGRESS);
        taskRequest.setDescription("changed1");

        mvc.perform(patch(TASK_URL + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk());

        TaskEntity taskEntity = taskRepository.findById(100L).get();
        assertEquals(taskRequest.getName(), taskEntity.getName());
        assertEquals(taskRequest.getDescription(), taskEntity.getDescription());
        assertEquals(taskRequest.getStatus().getValue(), taskEntity.getStatus().getValue());
    }

    @Test
    void changeTaskStatus() throws Exception {
        mvc.perform(put(TASK_URL + "/100/DONE"))
                .andExpect(status().isOk());

        TaskEntity taskEntity = taskRepository.findById(100L).get();
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    void testCreateTaskViolatingNameLengthConstraint() throws Exception {
        TaskRequest taskRequest = createTaskRequest();
        taskRequest.setName("na");

        mvc.perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTaskWithNullName() throws Exception {
        TaskRequest taskRequest = createTaskRequest();
        taskRequest.setName(null);

        mvc.perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTaskWithNameContainingSpecialCharacters() throws Exception {
        TaskRequest taskRequest = createTaskRequest();
        taskRequest.setName("name!@#");

        mvc.perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTaskViolatingDescriptionLengthConstraint() throws Exception {
        TaskRequest taskRequest = createTaskRequest();
        taskRequest.setDescription("desc");

        mvc.perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testChangeTaskWithWrongStatus() throws Exception {
        mvc.perform(put(TASK_URL + "/100/WrongStatus"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTaskWithSameNameAndStatusViolation() throws Exception {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("first");
        taskRequest.setStatus(Status.CREATED);

        mvc.perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTaskWithMaximumTaskLimitViolation() throws Exception {
        TaskRequest taskRequest = createTaskRequest();
        TaskEntity taskEntity = TaskEntity.builder()
                .name("test")
                .status(TaskStatus.CREATED)
                .description("testdesc")
                .build();

        taskRepository.save(taskEntity);
        mvc.perform(post(TASK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    private TaskRequest createTaskRequest() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("test");
        taskRequest.setDescription("description");
        taskRequest.setStatus(Status.CREATED);
        return taskRequest;
    }
}
