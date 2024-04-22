package com.pryvat.bank.task.manager;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"test"})
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
class TaskEntityManagerApplicationTests {

    @Test
    void contextLoads() {
    }

}
