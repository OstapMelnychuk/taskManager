package com.pryvat.bank.task.manager.service.task.impl;

import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.entity.task.TaskEntity;
import com.pryvat.bank.task.manager.entity.task.TaskStatus;
import com.pryvat.bank.task.manager.exception.EntityNotFoundException;
import com.pryvat.bank.task.manager.exception.WrongTaskStatusException;
import com.pryvat.bank.task.manager.filter.task.impl.TaskCountFilter;
import com.pryvat.bank.task.manager.filter.task.validator.TaskValidator;
import com.pryvat.bank.task.manager.model.TaskDTO;
import com.pryvat.bank.task.manager.repository.task.TaskRepository;
import com.pryvat.bank.task.manager.service.task.TaskService;
import com.pryvat.bank.task.manager.telegram.service.TelegramTaskUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link com.pryvat.bank.task.manager.service.task.TaskService}
 * Service that provides implementation of basic CRUD operations for Tasks
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class TaskServiceImpl implements TaskService {
    /**
     * Task repository to get task data from database
     */
    private final TaskRepository taskRepository;
    /**
     * Model mapper to map Task domain model to entity and vise versa
     */
    private final ModelMapper modelMapper;
    /**
     * Task Validator that validates business rules during task creation/updating
     */
    private final TaskValidator taskValidator;
    /**
     * Telegram service that provides updates when task is updated/created
     */
    private final TelegramTaskUpdateService telegramTaskUpdateService;
    private static final String MODEL_NAME = "Task";

    /**
     * Method that creates task. Executes different validations for business logic
     * @param task Domain model of a task. Should contain name, status and description for a task
     * @return id of created task
     * @throws com.pryvat.bank.task.manager.exception.TaskValidationException if any validation fails
     */
    public Long createTask(Task task) {
        taskValidator.validate(task);
        TaskEntity taskEntity = modelMapper.map(task, TaskEntity.class);
        TaskEntity savedEntity = taskRepository.save(taskEntity);
        log.info("Task with id %d has been created".formatted(savedEntity.getId()));
        telegramTaskUpdateService.sendTaskCreationMessage(modelMapper.map(savedEntity, Task.class));
        return savedEntity.getId();
    }

    /**
     * Method that deletes task from the database
     * @param id task id
     */
    public void deleteTask(Long id) {
        log.info("Deleting task with id %d".formatted(id));
        taskRepository.deleteById(id);
        log.info("Task with id %d has been deleted".formatted(id));
    }

    /**
     * Method that returns a list of all created tasks
     * @return a list of all created tasks
     */
    public List<TaskDTO> getAllTasks() {
        log.info("Returning all created tasks");
        return taskRepository.findAll().stream()
                .map(taskEntity -> modelMapper.map(taskEntity, TaskDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Method for updating task status
     * @param id task id
     * @param status status of the task {@link com.pryvat.bank.task.manager.entity.task.TaskStatus}
     * @throws WrongTaskStatusException if status task is invalid
     * @throws EntityNotFoundException if task is not found
     */
    public void updateTaskStatus(Long id, String status) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, MODEL_NAME));
        Task primaryTask = modelMapper.map(taskEntity, Task.class);
        TaskStatus taskStatus;

        try {
            taskStatus = TaskStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new WrongTaskStatusException(status);
        }
        Task taskToValidate = Task.builder()
                .name(taskEntity.getName())
                .status(taskStatus)
                .build();
        taskValidator.validate(taskToValidate, List.of(TaskCountFilter.class));
        taskEntity.setStatus(taskStatus);

        TaskEntity updatedEntity = taskRepository.save(taskEntity);
        log.info("Task status with id %d has been updated".formatted(id));
        telegramTaskUpdateService.sendTaskUpdatedMessage(primaryTask,
                modelMapper.map(updatedEntity, Task.class));
    }

    /**
     * Method that updates task fields and validates business logic rules to an updated task
     * @param task Domain model of a task. Should contain name, status and description for a task to be updated
     * @throws com.pryvat.bank.task.manager.exception.TaskValidationException if any validation fails
     * @throws EntityNotFoundException if task is not found
     */
    public void updateTaskFields(Task task) {
        taskValidator.validate(task, List.of(TaskCountFilter.class));
        TaskEntity taskEntity = taskRepository.findById(task.getId()).orElseThrow(()
                -> new EntityNotFoundException(task.getId(), MODEL_NAME));
        Task primaryTask = modelMapper.map(taskEntity, Task.class);

        taskEntity.setDescription(task.getDescription());
        taskEntity.setStatus(TaskStatus.valueOf(task.getStatus().getValue()));
        taskEntity.setName(task.getName());

        TaskEntity updatedEntity = taskRepository.save(taskEntity);
        log.info("Task fields with id %d have been updated".formatted(updatedEntity.getId()));
        telegramTaskUpdateService.sendTaskUpdatedMessage(primaryTask,
                modelMapper.map(updatedEntity, Task.class));
    }
}
