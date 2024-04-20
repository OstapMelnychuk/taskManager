package com.pryvat.bank.task.manager.service.task;

import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.model.TaskDTO;

import java.util.List;

public interface TaskService {
    Long createTask(Task task);
    void deleteTask(Long id);
    List<TaskDTO> getAllTasks();
    void updateTaskStatus(Long id, String status);
    void updateTaskFields(Task task);
}
