package com.pryvat.bank.task.manager.service.task;

import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.model.TaskDTO;

import java.util.List;

/**
 * TaskService provides basic CRUD operations for tasks
 */
public interface TaskService {
    /**
     * Method that is used for creating task
     * @param task Domain model of a task. Should contain name, status and description for a task
     * @return created task id
     */
    Long createTask(Task task);

    /**
     * Method that deletes task by id
     * @param id task id
     */
    void deleteTask(Long id);

    /**
     * Method that returns all created tasks
     * @return The list of all created tasks
     */
    List<TaskDTO> getAllTasks();

    /**
     * Method that updates task status
     * @param id task id
     * @param status status of the task {@link com.pryvat.bank.task.manager.entity.task.TaskStatus}
     */
    void updateTaskStatus(Long id, String status);

    /**
     * Method that updates task fields except id field
     * @param task Domain model of a task. Should contain name, status and description for a task to be updated
     */
    void updateTaskFields(Task task);
}
