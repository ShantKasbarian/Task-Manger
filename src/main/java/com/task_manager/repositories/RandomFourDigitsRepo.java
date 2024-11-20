package com.task_manager.repositories;

import com.task_manager.entities.RandomFourDigits;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomFourDigitsRepo extends JpaRepository<RandomFourDigits, Long> {
    RandomFourDigits findByNum(Integer num);
}
