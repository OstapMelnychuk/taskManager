package com.pryvat.bank.task.manager.repository.postgres.task;

import com.pryvat.bank.task.manager.entity.task.TaskEntity;
import com.pryvat.bank.task.manager.entity.task.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgresSqlTaskRepository extends JpaRepository<TaskEntity, Long> {
    boolean existsByNameAndStatus(String name, TaskStatus taskStatus);
}
