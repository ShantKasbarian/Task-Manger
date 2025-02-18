package com.task_manager.scheduler;

import com.task_manager.entities.RandomFourDigits;
import com.task_manager.repositories.RandomFourDigitsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DeleteExpiredCodeScheduler {

    private final RandomFourDigitsRepo randomFourDigitsRepo;

    @Autowired
    public DeleteExpiredCodeScheduler(RandomFourDigitsRepo randomFourDigitsRepo) {
        this.randomFourDigitsRepo = randomFourDigitsRepo;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void schedule() {
        List<RandomFourDigits> list = randomFourDigitsRepo.findAll().stream()
            .filter(randomFourDigits ->
                Duration.between(randomFourDigits.getSentTime(),
                LocalDateTime.now()).toMinutes() >= 5
            ).toList();

        randomFourDigitsRepo.deleteAll(list);

        System.err.println("deleted ");
    }
}
