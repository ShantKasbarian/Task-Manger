package com.task_manager.services;

import com.task_manager.converters.RequestConverter;
import com.task_manager.entities.Customer;
import com.task_manager.entities.Request;
import com.task_manager.entities.RequestStatus;
import com.task_manager.entities.Team;
import com.task_manager.exceptions.InvalidProvidedInfoException;
import com.task_manager.exceptions.RequestAlreadyTakenException;
import com.task_manager.exceptions.RequestUnauthorizedException;
import com.task_manager.exceptions.ResourceNotFoundException;
import com.task_manager.models.PageDto;
import com.task_manager.models.RequestDto;
import com.task_manager.repositories.RequestRepo;
import com.task_manager.repositories.TeamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    private final RequestRepo requestRepo;

    private final RequestConverter requestConverter;

    private final TeamRepo teamRepo;

    @Autowired
    public RequestService(
            RequestRepo requestRepo,
            RequestConverter requestConverter,
            TeamRepo teamRepo
    ) {
        this.requestRepo = requestRepo;
        this.requestConverter = requestConverter;
        this.teamRepo = teamRepo;
    }

    public Request createRequest(Request request) {
        request.setRequestStatus(RequestStatus.PENDING);

        if (request.getCustomer() == null) {
            throw new InvalidProvidedInfoException("customer id must be specified");
        }

        if (request.getDescription() == null || request.getDescription().trim().length() < 10) {
            throw new InvalidProvidedInfoException("request description must be greater than 10 characters");
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new InvalidProvidedInfoException("title must be specified");
        }

        return requestRepo.save(request);
    }

    public Request getRequestById(Long reqId, Long customerId) {
        Request request = requestRepo.findById(reqId)
                .orElseThrow(() -> new ResourceNotFoundException("request not found"));

        if (!request.getCustomer().getUserId().equals(customerId)) {
            throw new RequestUnauthorizedException("access denied");
        }

        return request;
    }

    public Request updateRequest(Request request) {
        Request oldReq = requestRepo.findById(request.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("request not found"));

        request.setRequestStatus(oldReq.getRequestStatus());

        if (request.getCustomer() == null) {
            throw new InvalidProvidedInfoException("customer id must be specified");
        }

        if (request.getDescription().trim().length() < 10) {
            throw new InvalidProvidedInfoException("request description must be greater than 10 characters");
        }

        if (request.getTitle() == null) {
            throw new InvalidProvidedInfoException("title must be specified");
        }

        return requestRepo.save(request);
    }

    public PageDto<RequestDto> getAllRequestsByCustomer(Customer customer, Pageable pageable) {
         Page<Request> page = requestRepo.findByCustomer(customer, pageable);

        List<RequestDto> requests = page.stream()
                .map(request -> requestConverter.convertToModel(request, new RequestDto()))
                .toList();

        PageDto<RequestDto> pageDto = new PageDto<>();
        pageDto.setContent(requests);
        pageDto.setPageNo(pageable.getPageSize());
        pageDto.setPageSize(page.getSize());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setEmpty(page.isEmpty());

        return pageDto;
    }

    public String deleteRequest(Long reqId, Long customerId) {
        Request request = requestRepo.findById(reqId)
                .orElseThrow(() -> new ResourceNotFoundException("request not found"));

        if (!request.getCustomer().getUserId().equals(customerId)) {
            throw new RequestUnauthorizedException("unauthorized");
        }

        requestRepo.delete(request);

        return "request has been deleted";
    }

    public PageDto<RequestDto> getAllPendingRequests(Pageable pageable) {
        Page<Request> page = requestRepo.findByRequestStatus(RequestStatus.PENDING, pageable);

        List<RequestDto> requests = page.stream()
                .map(request -> requestConverter.convertToModel(request, new RequestDto()))
                .toList();

        PageDto<RequestDto> pageDto = new PageDto<>();
        pageDto.setContent(requests);
        pageDto.setPageNo(pageable.getPageSize());
        pageDto.setPageSize(page.getSize());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setEmpty(page.isEmpty());

        return pageDto;
    }

    public Request takeRequest(Long reqId, Team team) {
        Request request = requestRepo.findById(reqId)
                .orElseThrow(() -> new ResourceNotFoundException("request not found"));

        if (request.getTeam() != null) {
            throw new RequestAlreadyTakenException("this request is already taken");
        }

        if (!request.getRequestStatus().equals(RequestStatus.ACCEPTED)) {
            throw new ResourceNotFoundException("request is not accepted yet");
        }

        request.setTeam(team);

        return requestRepo.save(request);
    }

    public Request changeRequestStatus(RequestStatus requestStatus, Long reqId, Long teamId) {
        Request request = requestRepo.findById(reqId)
                .orElseThrow(() -> new ResourceNotFoundException("request not found"));

        if (!request.getTeam().getTeamId().equals(teamId)) {
            throw new RequestAlreadyTakenException("request has been taken by another team or has not been taken");
        }

        if (requestStatus.equals(RequestStatus.REJECTED)) {
            throw new RequestUnauthorizedException("only admin can reject requests");
        }

        request.setRequestStatus(requestStatus);

        return requestRepo.save(request);
    }

    public Request rejectRequest(Long reqId) {
        Request request = requestRepo.findById(reqId)
                .orElseThrow(() -> new NullPointerException("request not found"));

        request.setRequestStatus(RequestStatus.REJECTED);

        return requestRepo.save(request);
    }

    public Request acceptRequest(Long reqId) {
        Request request = requestRepo.findById(reqId)
                .orElseThrow(() -> new NullPointerException("request not found"));

        request.setRequestStatus(RequestStatus.ACCEPTED);

        return requestRepo.save(request);
    }

    public PageDto<RequestDto> getAllAcceptedRequest(Pageable pageable) {
        Page<Request> page = requestRepo.findByRequestStatus(RequestStatus.ACCEPTED, pageable);

        List<RequestDto> requests = page.stream()
                .map(request -> requestConverter.convertToModel(request, new RequestDto()))
                .toList();

        PageDto<RequestDto> pageDto = new PageDto<>();
        pageDto.setContent(requests);
        pageDto.setPageNo(pageable.getPageSize());
        pageDto.setPageSize(page.getSize());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setEmpty(page.isEmpty());

        return pageDto;
    }

    public PageDto<RequestDto> getRequestsByTeam(Team team, Pageable pageable) {
        Page<Request> page = requestRepo.findByTeam(team, pageable);

        List<RequestDto> requests = page.stream()
                .map(request -> requestConverter.convertToModel(request, new RequestDto()))
                .toList();

        PageDto<RequestDto> pageDto = new PageDto<>();
        pageDto.setContent(requests);
        pageDto.setPageNo(pageable.getPageSize());
        pageDto.setPageSize(page.getSize());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setEmpty(page.isEmpty());

        return pageDto;
    }
}
