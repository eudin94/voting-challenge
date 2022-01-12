package com.comerlato.voting_challenge.service;

import com.comerlato.voting_challenge.helper.MessageHelper;
import com.comerlato.voting_challenge.modules.entity.Schedule;
import com.comerlato.voting_challenge.modules.repository.ScheduleRepository;
import com.comerlato.voting_challenge.modules.repository.ScheduleResultsRepository;
import com.comerlato.voting_challenge.modules.repository.VoteRepository;
import com.comerlato.voting_challenge.modules.repository.specification.ScheduleSpecification;
import com.comerlato.voting_challenge.mq.MessageSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.comerlato.voting_challenge.creator.AssociateCreator.associate;
import static com.comerlato.voting_challenge.creator.AssociateCreator.associateDTO;
import static com.comerlato.voting_challenge.creator.ScheduleCreator.*;
import static com.comerlato.voting_challenge.creator.VoteCreator.*;
import static com.comerlato.voting_challenge.enums.VoteAnswerEnum.NO;
import static com.comerlato.voting_challenge.enums.VoteAnswerEnum.YES;
import static com.comerlato.voting_challenge.util.mapper.MapperConstants.scheduleMapper;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
public class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService service;
    @Mock
    private ScheduleRepository repository;
    @Mock
    private AssociateService associateService;
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private ScheduleResultsRepository resultsRepository;
    @Mock
    private TransactionTemplate transaction;
    @Mock
    private MessageSender messageSender;
    @Mock
    private MessageHelper messageHelper;

    @Test
    void create_returnsScheduleDTO_whenSuccessful() {
        when(repository.save(schedule.withId(null))).thenReturn(schedule);
        when(repository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        assertEquals(scheduleDTO, service.create(scheduleRequestDTO));
    }

    @Test
    void findAll_returnsPageOfDTOs_whenSuccessful() {
        final var page = new PageImpl<Schedule>(List.of(schedule));
        final var assertion = page.map(scheduleMapper::buildScheduleDTO);
        final var pageable = PageRequest.of(0, 10, Sort.by(ASC, "id"));
        when(repository.findAll(any(ScheduleSpecification.class), any(Pageable.class))).thenReturn(page);
        assertEquals(assertion, service.findAll(empty(), empty(), pageable));
    }

    @Test
    void closeSchedule_returnsScheduleResultsYes_whenSuccessful() {
        when(transaction.execute(any())).thenAnswer(invocationOnMock -> invocationOnMock
                .<TransactionCallback<Boolean>>getArgument(0).doInTransaction(mock(TransactionStatus.class)));
        when(repository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(repository.save(schedule.withClosed(true))).thenReturn(schedule.withClosed(true));
        when(resultsRepository.save(resultsYes)).thenReturn(resultsYes);
        when(voteRepository.countByAnswerAndScheduleId(YES, schedule.getId())).thenReturn(1L);
        when(voteRepository.countByAnswerAndScheduleId(NO, schedule.getId())).thenReturn(0L);
        when(resultsRepository.findById(resultsYes.getScheduleId())).thenReturn(Optional.of(resultsYes));
        assertEquals(resultsDTOYes, service.closeSchedule(schedule.getId()));
    }

    @Test
    void closeSchedule_returnsScheduleResultsNo_whenSuccessful() {
        when(transaction.execute(any())).thenAnswer(invocationOnMock -> invocationOnMock
                .<TransactionCallback<Boolean>>getArgument(0).doInTransaction(mock(TransactionStatus.class)));
        when(repository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(repository.save(schedule.withClosed(true))).thenReturn(schedule.withClosed(true));
        when(resultsRepository.save(resultsNo)).thenReturn(resultsNo);
        when(voteRepository.countByAnswerAndScheduleId(YES, schedule.getId())).thenReturn(0L);
        when(voteRepository.countByAnswerAndScheduleId(NO, schedule.getId())).thenReturn(1L);
        when(resultsRepository.findById(resultsNo.getScheduleId())).thenReturn(Optional.of(resultsNo));
        assertEquals(resultsDTONo, service.closeSchedule(schedule.getId()));
    }

    @Test
    void closeSchedule_throws400_whenScheduleAlreadyClosed() {
        when(transaction.execute(any())).thenAnswer(invocationOnMock -> invocationOnMock
                .<TransactionCallback<Boolean>>getArgument(0).doInTransaction(mock(TransactionStatus.class)));
        when(repository.findById(schedule.getId())).thenReturn(Optional.of(schedule.withClosed(true)));
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.closeSchedule(schedule.getId())).getStatus();
        assertEquals(BAD_REQUEST, status);
    }

    @Test
    void voteForSchedule_returnsVoteDTO_whenSuccessful() {
        when(repository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(associateService.findDTOById(associate.getId())).thenReturn(associateDTO);
        when(voteRepository.findByScheduleIdAndAssociateId(schedule.getId(), associate.getId())).thenReturn(empty());
        when(voteRepository.save(vote.withId(null))).thenReturn(vote);
        when(voteRepository.findById(vote.getId())).thenReturn(Optional.of(vote));
        assertEquals(voteDTO, service.voteForSchedule(voteRequestDTO));
    }

    @Test
    void voteForSchedule_returns400_whenScheduleIsClosed() {
        when(repository.findById(schedule.getId())).thenReturn(Optional.of(schedule.withClosed(true)));
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.voteForSchedule(voteRequestDTO)).getStatus();
        assertEquals(BAD_REQUEST, status);
    }

    @Test
    void voteForSchedule_returns400_whenAssociateAlreadyVoted() {
        when(repository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(associateService.findDTOById(associate.getId())).thenReturn(associateDTO);
        when(voteRepository.findByScheduleIdAndAssociateId(schedule.getId(), associate.getId()))
                .thenReturn(Optional.of(vote));
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.voteForSchedule(voteRequestDTO)).getStatus();
        assertEquals(BAD_REQUEST, status);
    }

    @Test
    void findScheduleResultsDTOById_returns404_whenResultsNotFound() {
        when(resultsRepository.findById(resultsYes.getScheduleId())).thenReturn(empty());
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.findScheduleResultsDTOById(resultsYes.getScheduleId())).getStatus();
        assertEquals(NOT_FOUND, status);
    }

    @Test
    void findVoteDTOById_returns404_whenVoteNotFound() {
        when(voteRepository.findById(vote.getId())).thenReturn(empty());
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.findVoteDTOById(vote.getId())).getStatus();
        assertEquals(NOT_FOUND, status);
    }

    @Test
    void findDTOById_returns404_whenScheduleNotFound() {
        when(repository.findById(schedule.getId())).thenReturn(empty());
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.findDTOById(schedule.getId())).getStatus();
        assertEquals(NOT_FOUND, status);
    }
}
