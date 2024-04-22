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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;
    private final TaskValidator taskValidator;
    private final TelegramTaskUpdateService telegramTaskUpdateService;
    private static final String MODEL_NAME = "Task";

    public Long createTask(Task task) {
        taskValidator.validate(task);
        TaskEntity taskEntity = modelMapper.map(task, TaskEntity.class);
        TaskEntity savedEntity = taskRepository.save(taskEntity);
        telegramTaskUpdateService.sendTaskCreationMessage(modelMapper.map(savedEntity, Task.class));
        return savedEntity.getId();
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskEntity -> modelMapper.map(taskEntity, TaskDTO.class))
                .collect(Collectors.toList());
    }

    public void updateTaskStatus(Long id, String status) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, MODEL_NAME));
        Task primaryTask = modelMapper.map(taskEntity, Task.class);

        try {
            taskEntity.setStatus(TaskStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new WrongTaskStatusException(status);
        }

        TaskEntity updatedEntity = taskRepository.save(taskEntity);
        telegramTaskUpdateService.sendTaskUpdatedMessage(primaryTask,
                modelMapper.map(updatedEntity, Task.class));
    }

    public void updateTaskFields(Task task) {
        taskValidator.validate(task, List.of(TaskCountFilter.class));
        TaskEntity taskEntity = taskRepository.findById(task.getId()).orElseThrow(()
                -> new EntityNotFoundException(task.getId(), MODEL_NAME));
        Task primaryTask = modelMapper.map(taskEntity, Task.class);

        taskEntity.setDescription(task.getDescription());
        taskEntity.setStatus(TaskStatus.valueOf(task.getStatus().getValue()));
        taskEntity.setName(task.getName());

        TaskEntity updatedEntity = taskRepository.save(taskEntity);
        telegramTaskUpdateService.sendTaskUpdatedMessage(primaryTask,
                modelMapper.map(updatedEntity, Task.class));
    }
}
