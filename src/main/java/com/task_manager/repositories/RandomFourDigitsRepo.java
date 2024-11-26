package com.task_manager.repositories;

import com.task_manager.entities.RandomFourDigits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RandomFourDigitsRepo extends JpaRepository<RandomFourDigits, Long> {
    RandomFourDigits findByNum(Integer num);
    RandomFourDigits findByToken(String token);
}
