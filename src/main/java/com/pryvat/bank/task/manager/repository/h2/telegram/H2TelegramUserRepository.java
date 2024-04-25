package com.pryvat.bank.task.manager.repository.h2.telegram;

import com.pryvat.bank.task.manager.entity.telegram.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface H2TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
}
