package com.pryvat.bank.task.manager.repository.telegram;

import com.pryvat.bank.task.manager.entity.telegram.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
}
