package com.task_manager.controllers;

import com.task_manager.converters.RequestConverter;
import com.task_manager.entities.*;
import com.task_manager.models.RequestDto;
import com.task_manager.models.RequestStatusDto;
import com.task_manager.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request")
public class RequestController {

    private final RequestService requestService;

    private final RequestConverter requestConverter;

    @Autowired
    public RequestController(
            RequestService requestService,
            RequestConverter requestConverter
    ) {
        this.requestService = requestService;
        this.requestConverter = requestConverter;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/create")
    public ResponseEntity<RequestDto> createRequest(
            Authentication authentication,
            @RequestBody RequestDto requestDto
    ) {

        Customer user = (Customer) authentication.getPrincipal();

        Request request = requestConverter.convertToEntity(new Request(), requestDto);
        request.setCustomer(user);

        return new ResponseEntity<>(
                requestConverter.convertToModel(
                        requestService.createRequest(request), new RequestDto()
                ), HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{reqId}")
    public ResponseEntity<?> getRequestById(
            Authentication authentication,
            @PathVariable Long reqId
    ) {
        Customer customer = (Customer) authentication.getPrincipal();

        return ResponseEntity.ok(
                requestConverter.convertToModel(
                        requestService.getRequestById(reqId, customer.getUserId()),
                        new RequestDto()
                )
        );
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/update")
    public ResponseEntity<?> updateRequest(
            Authentication authentication,
            @RequestBody RequestDto requestDto
    ) {
        Customer customer = (Customer) authentication.getPrincipal();

        Request request = requestConverter.convertToEntity(new Request(), requestDto);

        request.setCustomer(customer);

        return ResponseEntity.ok(
                requestConverter.convertToModel(
                        requestService.updateRequest(request), new RequestDto()
                )
        );
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllCustomerRequests(
            Authentication authentication,
            @RequestParam(value = "num", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        Customer customer = (Customer) authentication.getPrincipal();

        Pageable pageable = PageRequest.of(pageNum, size);

        return ResponseEntity.ok(requestService.getAllRequestsByCustomer(customer, pageable));
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @DeleteMapping("/{reqId}/delete")
    public ResponseEntity<String> deleteRequest(
            Authentication authentication,
            @PathVariable Long reqId
    ) {
        User user = (User) authentication.getPrincipal();

        return new ResponseEntity<>(
                requestService.deleteRequest(reqId, user.getUserId()),
                HttpStatus.NO_CONTENT
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<?> getAllPendingRequests(
            @RequestParam(value = "num", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(pageNum, size);

        return ResponseEntity.ok(requestService.getAllPendingRequests(pageable));
    }

    @PreAuthorize("hasRole('TEAM_LEAD')")
    @PutMapping("/{reqId}/take")
    public ResponseEntity<?> takeRequest(
            Authentication authentication,
            @PathVariable Long reqId
    ) {
        TeamLead teamLead = (TeamLead) authentication.getPrincipal();

        return ResponseEntity.ok(requestService.takeRequest(reqId, teamLead.getTeam()));
    }

    @PreAuthorize("hasRole('TEAM_LEAD')")
    @PutMapping("/{reqId}/status")
    public ResponseEntity<?> changeStatus(
            Authentication authentication,
            @RequestBody RequestStatusDto requestStatusDto,
            @PathVariable Long reqId
    ) {
        TeamLead teamLead = (TeamLead) authentication.getPrincipal();

        return ResponseEntity.ok(
            requestService.changeRequestStatus(
                requestStatusDto.getRequestStatus(),
                reqId,
                teamLead.getTeam().getTeamId()
            )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{reqId}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Long reqId) {
        return ResponseEntity.ok(requestConverter.convertToModel(
                requestService.rejectRequest(reqId),
                new RequestDto()
            )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{reqId}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable Long reqId) {
        return new ResponseEntity<>(
                requestConverter.convertToModel(
                        requestService.acceptRequest(reqId),
                        new RequestDto()
                ), HttpStatus.ACCEPTED
        );
    }

    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN')")
    @GetMapping("/accepted")
    public ResponseEntity<?> getAllAcceptedRequests(
            @RequestParam(value = "num", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(pageNum, size);
        return ResponseEntity.ok(requestService.getAllAcceptedRequest(pageable));
    }
}
