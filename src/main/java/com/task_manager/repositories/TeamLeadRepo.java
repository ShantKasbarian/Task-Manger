package com.task_manager.repositories;

import com.task_manager.entities.TeamLead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamLeadRepo extends JpaRepository<TeamLead, Long> {
}
