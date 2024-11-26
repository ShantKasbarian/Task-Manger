package com.task_manager.repositories;

import com.task_manager.entities.Customer;
import com.task_manager.entities.Request;
import com.task_manager.entities.RequestStatus;
import com.task_manager.entities.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepo extends JpaRepository<Request, Long> {
    Page<Request> findByCustomer(Customer customer, Pageable pageable);

    Page<Request> findByRequestStatus(RequestStatus requestStatus, Pageable pageable);

    Page<Request> findByTeam(Team team, Pageable pageable);
}
