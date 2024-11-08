package com.task_manager.repositories;

import com.task_manager.entities.Customer;
import com.task_manager.entities.Request;
import com.task_manager.entities.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepo extends JpaRepository<Request, Long> {
    Page<Request> findByCustomer(Customer customer, Pageable pageable);

    Page<Request> findByRequestStatus(RequestStatus requestStatus, Pageable pageable);
}
