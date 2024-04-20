package com.pryvat.bank.task.manager.delegate;

import com.pryvat.bank.task.manager.controller.TasksApiDelegate;
import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.model.TaskDTO;
import com.pryvat.bank.task.manager.model.TaskRequest;
import com.pryvat.bank.task.manager.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskDelegate implements TasksApiDelegate {
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Void> changeTaskFields(Long id, TaskRequest taskRequest) {
        Task task = modelMapper.map(taskRequest, Task.class);
        task.setId(id);
        taskService.updateTaskFields(task);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Long> createTask(TaskRequest taskRequest) {
        Task task = modelMapper.map(taskRequest, Task.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Override
    public ResponseEntity<Void> updateTaskStatus(Long id, String status) {
        taskService.updateTaskStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
