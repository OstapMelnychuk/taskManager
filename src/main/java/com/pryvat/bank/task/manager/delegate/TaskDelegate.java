package com.pryvat.bank.task.manager.delegate;

import com.pryvat.bank.task.manager.controller.TaskApiDelegate;
import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.model.TaskDTO;
import com.pryvat.bank.task.manager.model.TaskRequest;
import com.pryvat.bank.task.manager.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of OpenAPI {@link TaskApiDelegate}.
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class TaskDelegate implements TaskApiDelegate {
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Void> changeTaskFields(Long id, TaskRequest taskRequest) {
        log.info("Received request for changing task fields with id: %d".formatted(id));
        Task task = modelMapper.map(taskRequest, Task.class);
        task.setId(id);
        taskService.updateTaskFields(task);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Long> createTask(TaskRequest taskRequest) {
        log.info("Received request for task creation");
        Task task = modelMapper.map(taskRequest, Task.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        log.info("Received request for task deleting");
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        log.info("Received request for getting all the tasks");
        List<TaskDTO> taskDTOList = taskService.getAllTasks().stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .toList();
        return ResponseEntity.ok(taskDTOList);
    }

    @Override
    public ResponseEntity<Void> updateTaskStatus(Long id, String status) {
        log.info("Received request for updating task status with id %d".formatted(id));
        taskService.updateTaskStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
