package com.task_manager.repositories;

import com.task_manager.entities.Employee;
import com.task_manager.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    Page<Task> getTasksByEmployee(Employee employee, Pageable pageable);
}
