package com.task_manager.services;

import com.task_manager.converters.RequestConverter;
import com.task_manager.entities.Customer;
import com.task_manager.entities.Request;
import com.task_manager.entities.RequestStatus;
import com.task_manager.entities.Team;
import com.task_manager.models.PageDto;
import com.task_manager.models.RequestDto;
import com.task_manager.repositories.RequestRepo;
import com.task_manager.repositories.TeamRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RequestServiceTest {
    @InjectMocks
    private RequestService requestService;

    @Mock
    private RequestRepo requestRepo;

    @Mock
    private RequestConverter requestConverter;

    @Mock
    private TeamRepo teamRepo;

    private Request request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new Request();

        request.setRequestId(1L);
        request.setTitle("some title");
        request.setDescription("huifheiubwf wiohdiupdhq dqhiuqhbdib qdiqdniudniubq qdbduibdiuqbdidnqubn ");
        request.setCustomer(new Customer());
        request.getCustomer().setUserId(1L);
        request.setRequestStatus(RequestStatus.PENDING);
        request.getCustomer().setRequests(new ArrayList<>());
    }

    @Test
    void createRequest() {
        when(requestRepo.save(request)).thenReturn(request);

        Request response = requestService.createRequest(request);

        assertEquals(request.getRequestId(), response.getRequestId());
        assertEquals(RequestStatus.PENDING, response.getRequestStatus());
        assertEquals(request.getDescription(), response.getDescription());
    }

    @Test
    void createRequestShouldReturnCustomerIdMustBeSpecified() {
        request.setCustomer(null);
        assertThrows(NullPointerException.class, () -> requestService.createRequest(request));
    }

    @Test
    void createRequestShouldReturnRequestDescriptionMustBeGreaterThan40Characters() {
        request.setDescription("some desc");
        assertThrows(RuntimeException.class, () -> requestService.createRequest(request));
    }

    @Test
    void createRequestShouldReturnTitleMustBeSpecified() {
        request.setTitle(null);
        assertThrows(NullPointerException.class, () -> requestService.createRequest(request));
    }

    @Test
    void getRequestById() {
        when(requestRepo.findById(1L)).thenReturn(Optional.ofNullable(request));
        Request response = requestService.getRequestById(1L, 1L);

        assertEquals(request.getRequestId(), response.getRequestId());
        assertEquals(request.getDescription(), response.getDescription());
    }

    @Test
    void updateRequest() {
        Request oldReq = new Request();
        oldReq.setRequestId(1L);
        oldReq.setTitle("title");
        oldReq.setDescription(request.getDescription() + "aaa");
        oldReq.setCustomer(request.getCustomer());
        oldReq.setRequestStatus(RequestStatus.PENDING);

        when(requestRepo.findById(1L)).thenReturn(Optional.of(oldReq));
        when(requestRepo.save(request)).thenReturn(request);

        Request response = requestService.updateRequest(request);

        assertEquals(oldReq.getRequestId(), response.getRequestId());
        assertNotEquals(oldReq.getTitle(), response.getTitle());
        assertNotEquals(oldReq.getDescription(), response.getDescription());
    }

    @Test
    void getAllRequestsByCustomer() {
        request.getCustomer().getRequests().add(new Request());
        request.getCustomer().getRequests().add(new Request());

        Page<Request> page = new PageImpl<>(request.getCustomer().getRequests());
        Pageable pageable = PageRequest.of(0, 10);

        when(requestRepo.findByCustomer(request.getCustomer(), pageable)).thenReturn(page);
        when(requestConverter.convertToModel(
                request.getCustomer().getRequests().getFirst(), new RequestDto()))
                .thenReturn(new RequestDto());
        when(requestConverter.convertToModel(
                request.getCustomer().getRequests().get(1), new RequestDto()))
                .thenReturn(new RequestDto());


        PageDto<RequestDto> response = requestService.getAllRequestsByCustomer(request.getCustomer(), pageable);

        assertEquals(request.getCustomer().getRequests().size(), response.getContent().size());
    }

    @Test
    void deleteRequest() {
        when(requestRepo.findById(1L)).thenReturn(Optional.ofNullable(request));
        String response = requestService.deleteRequest(1L, 1L);

        assertEquals("request has been deleted", response);
    }

    @Test
    void getAllPendingRequests() {
        request.getCustomer().getRequests().add(new Request());
        request.getCustomer().getRequests().get(0).setRequestStatus(RequestStatus.PENDING);
        request.getCustomer().getRequests().add(new Request());
        request.getCustomer().getRequests().get(1).setRequestStatus(RequestStatus.PENDING);

        Page<Request> page = new PageImpl<>(request.getCustomer().getRequests());
        Pageable pageable = PageRequest.of(0, 10);

        when(requestRepo.findByRequestStatus(RequestStatus.PENDING, pageable)).thenReturn(page);
        when(requestConverter.convertToModel(
                request.getCustomer().getRequests().get(0), new RequestDto()))
                .thenReturn(new RequestDto());
        when(requestConverter.convertToModel(
                request.getCustomer().getRequests().get(1), new RequestDto()))
                .thenReturn(new RequestDto());

        PageDto<RequestDto> response = requestService.getAllPendingRequests(pageable);

        assertEquals(request.getCustomer().getRequests().size(), response.getContent().size());
    }

    @Test
    void takeRequest() {
        request.setRequestStatus(RequestStatus.ACCEPTED);
        Team team = new Team();

        when(requestRepo.findById(1L)).thenReturn(Optional.ofNullable(request));
        when(requestRepo.save(request)).thenReturn(request);

        Request response = requestService.takeRequest(1L, team);

        assertEquals(request.getRequestId(), response.getRequestId());
        assertEquals(team, response.getTeam());
        assertEquals(RequestStatus.ACCEPTED, response.getRequestStatus());
    }

    @Test
    void changeRequestStatus() {
        request.setTeam(new Team());
        request.getTeam().setTeamId(1L);

        when(requestRepo.findById(1L)).thenReturn(Optional.ofNullable(request));
        when(requestRepo.save(request)).thenReturn(request);

        Request response = requestService.changeRequestStatus(RequestStatus.DONE, 1L, 1L);

        assertEquals(request.getRequestId(), response.getRequestId());
        assertNotEquals(RequestStatus.PENDING, response.getRequestStatus());
    }

    @Test
    void rejectRequest() {
        when(requestRepo.findById(1L)).thenReturn(Optional.ofNullable(request));
        when(requestRepo.save(request)).thenReturn(request);

        Request response = requestService.rejectRequest(1L);

        assertEquals(RequestStatus.REJECTED, response.getRequestStatus());
    }

    @Test
    void acceptRequest() {
        when(requestRepo.findById(1L)).thenReturn(Optional.ofNullable(request));
        when(requestRepo.save(request)).thenReturn(request);

        Request response = requestService.acceptRequest(1L);

        assertEquals(RequestStatus.ACCEPTED, response.getRequestStatus());
    }

    @Test
    void getAllAcceptedRequest() {
        request.getCustomer().getRequests().add(new Request());
        request.getCustomer().getRequests().get(0).setRequestStatus(RequestStatus.ACCEPTED);
        request.getCustomer().getRequests().add(new Request());
        request.getCustomer().getRequests().get(1).setRequestStatus(RequestStatus.ACCEPTED);

        Page<Request> page = new PageImpl<>(request.getCustomer().getRequests());
        Pageable pageable = PageRequest.of(0, 10);

        when(requestRepo.findByRequestStatus(RequestStatus.ACCEPTED, pageable)).thenReturn(page);
        when(requestConverter.convertToModel(
                request.getCustomer().getRequests().get(0), new RequestDto()))
                .thenReturn(new RequestDto());
        when(requestConverter.convertToModel(
                request.getCustomer().getRequests().get(1), new RequestDto()))
                .thenReturn(new RequestDto());

        PageDto<RequestDto> response = requestService.getAllAcceptedRequest(pageable);

        assertEquals(request.getCustomer().getRequests().size(), response.getContent().size());
    }

    @Test
    void getRequestsByTeam() {
        request.setTeam(new Team());
        request.getTeam().setTeamId(1L);
        request.getTeam().setRequests(new ArrayList<>());

        request.getTeam().getRequests().add(new Request());
        request.getTeam().getRequests().get(0).setRequestStatus(RequestStatus.ACCEPTED);
        request.getTeam().getRequests().add(new Request());
        request.getTeam().getRequests().get(1).setRequestStatus(RequestStatus.ACCEPTED);

        Page<Request> page = new PageImpl<>(request.getTeam().getRequests());
        Pageable pageable = PageRequest.of(0, 10);

        when(requestRepo.findByTeam(request.getTeam(), pageable)).thenReturn(page);
        when(requestConverter.convertToModel(
                request.getTeam().getRequests().get(0), new RequestDto()))
                .thenReturn(new RequestDto());
        when(requestConverter.convertToModel(
                request.getTeam().getRequests().get(1), new RequestDto()))
                .thenReturn(new RequestDto());

        PageDto<RequestDto> response = requestService.getRequestsByTeam(request.getTeam(), pageable);

        assertEquals(request.getTeam().getRequests().size(), response.getContent().size());
    }
}