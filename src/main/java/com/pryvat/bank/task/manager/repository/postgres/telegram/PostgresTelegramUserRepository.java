package com.pryvat.bank.task.manager.repository.postgres.telegram;

import com.pryvat.bank.task.manager.entity.telegram.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgresTelegramUserRepository extends JpaRepository<TelegramUser, Long> {
}
