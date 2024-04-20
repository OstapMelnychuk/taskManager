package com.pryvat.bank.task.manager.service.task.impl;

import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.entity.task.TaskEntity;
import com.pryvat.bank.task.manager.entity.task.TaskStatus;
import com.pryvat.bank.task.manager.exception.EntityNotFoundException;
import com.pryvat.bank.task.manager.filter.task.TaskCountFilter;
import com.pryvat.bank.task.manager.filter.task.TaskValidator;
import com.pryvat.bank.task.manager.model.TaskDTO;
import com.pryvat.bank.task.manager.repository.TaskRepository;
import com.pryvat.bank.task.manager.service.task.TaskService;
import jakarta.validation.Valid;
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
    private static final String MODEL_NAME = "Task";

    public Long createTask(@Valid Task task) {
        taskValidator.validate(task);
        TaskEntity taskEntity = modelMapper.map(task, TaskEntity.class);
        TaskEntity savedEntity = taskRepository.save(taskEntity);
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
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(id, MODEL_NAME));
        taskEntity.setStatus(TaskStatus.valueOf(status));
        taskRepository.save(taskEntity);
    }

    public void updateTaskFields(@Valid Task task) {
        taskValidator.validate(task, List.of(TaskCountFilter.class));
        TaskEntity taskEntity = taskRepository.findById(task.getId()).orElseThrow(()
                -> new EntityNotFoundException(task.getId(), MODEL_NAME));
        taskEntity.setDescription(task.getDescription());
        taskEntity.setStatus(TaskStatus.valueOf(task.getStatus().getValue()));
        taskRepository.save(taskEntity);
    }
}
