package com.pryvat.bank.task.manager.repository.h2.task;

import com.pryvat.bank.task.manager.entity.task.TaskEntity;
import com.pryvat.bank.task.manager.entity.task.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface H2TaskRepository extends JpaRepository<TaskEntity, Long> {
    boolean existsByNameAndStatus(String name, TaskStatus taskStatus);
}
