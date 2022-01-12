package com.comerlato.voting_challenge.service;

import com.comerlato.voting_challenge.dto.*;
import com.comerlato.voting_challenge.enums.VoteResultEnum;
import com.comerlato.voting_challenge.helper.MessageHelper;
import com.comerlato.voting_challenge.modules.entity.Schedule;
import com.comerlato.voting_challenge.modules.entity.ScheduleResults;
import com.comerlato.voting_challenge.modules.entity.Vote;
import com.comerlato.voting_challenge.modules.repository.ScheduleRepository;
import com.comerlato.voting_challenge.modules.repository.ScheduleResultsRepository;
import com.comerlato.voting_challenge.modules.repository.VoteRepository;
import com.comerlato.voting_challenge.modules.repository.specification.ScheduleSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.comerlato.voting_challenge.enums.VoteAnswerEnum.NO;
import static com.comerlato.voting_challenge.enums.VoteAnswerEnum.YES;
import static com.comerlato.voting_challenge.exception.ErrorCodeEnum.*;
import static com.comerlato.voting_challenge.util.mapper.MapperConstants.*;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {

    private final AssociateService associateService;
    private final ScheduleRepository repository;
    private final VoteRepository voteRepository;
    private final ScheduleResultsRepository scheduleResultsRepository;
    private final TransactionTemplate transaction;
    private final MessageHelper messageHelper;

    public ScheduleDTO create(final ScheduleRequestDTO request) {
        final var savedSchedule = repository.save(scheduleMapper.buildSchedule(request).withClosed(false)
                .withDurationInSeconds(nonNull(request.getDurationInSeconds()) ? request.getDurationInSeconds() : 60));
        return findDTOById(savedSchedule.getId());
    }

    public Page<ScheduleDTO> findAll(final Optional<String> subject, final Optional<Boolean> closed, Pageable pageable) {
        return repository.findAll(ScheduleSpecification.builder()
                .subject(subject)
                .closed(closed)
                .build(), pageable).map(scheduleMapper::buildScheduleDTO);
    }

    public ScheduleDTO findDTOById(final Long scheduleId) {
        return scheduleMapper.buildScheduleDTO(findById(scheduleId));
    }

    public ScheduleResultsDTO closeSchedule(final Long scheduleId) {
        return transaction.execute(status -> {
            final var schedule = findById(scheduleId);
            if (TRUE.equals(schedule.getClosed())) {
                log.error(messageHelper.get(ERROR_CLOSED_SCHEDULE));
                throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_CLOSED_SCHEDULE));
            }
            final var closedSchedule = repository.save(schedule.withClosed(true));
            return createScheduleResults(closedSchedule.getId());
        });
    }

    public VoteDTO voteForSchedule(final VoteRequestDTO request) {
        validateVote(request.getScheduleId(), request.getAssociateId());
        final var savedVote = voteRepository.save(voteMapper.buildVote(request));
        return findVoteDTOById(savedVote.getId());
    }

    public ScheduleResultsDTO findScheduleResultsDTOById(final Long scheduleId) {
        return scheduleResultsMapper.buildScheduleResultsDTO(findScheduleResultsById(scheduleId));
    }

    public VoteDTO findVoteDTOById(final Long voteId) {
        return voteMapper.buildVoteDTO(findVoteById(voteId));
    }

    private void validateVote(final Long scheduleId, final Long associateId) {
        final var schedule = findDTOById(scheduleId);
        if (TRUE.equals(schedule.getClosed())) {
            log.error(messageHelper.get(ERROR_CLOSED_SCHEDULE));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_CLOSED_SCHEDULE));
        }
        final var associate = associateService.findDTOById(associateId);
        final var existingVote = voteRepository
                .findByScheduleIdAndAssociateId(schedule.getId(), associate.getId());
        if (existingVote.isPresent()) {
            log.error(messageHelper.get(ERROR_VOTE_ALREADY_EXISTS));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_VOTE_ALREADY_EXISTS));
        }
    }

    private Schedule findById(final Long scheduleId) {
        return repository.findById(scheduleId).orElseThrow(() -> {
            log.error(messageHelper.get(ERROR_SCHEDULE_NOT_FOUND));
            throw new ResponseStatusException(NOT_FOUND, messageHelper.get(ERROR_SCHEDULE_NOT_FOUND));
        });
    }

    private Vote findVoteById(final Long voteId) {
        return voteRepository.findById(voteId).orElseThrow(() -> {
            log.error(messageHelper.get(ERROR_VOTE_NOT_FOUND));
            throw new ResponseStatusException(NOT_FOUND, messageHelper.get(ERROR_VOTE_NOT_FOUND));
        });
    }

    private ScheduleResults findScheduleResultsById(final Long scheduleId) {
        return scheduleResultsRepository.findById(scheduleId).orElseThrow(() -> {
            log.error(messageHelper.get(ERROR_SCHEDULE_RESULTS_NOT_FOUND));
            throw new ResponseStatusException(NOT_FOUND, messageHelper.get(ERROR_SCHEDULE_RESULTS_NOT_FOUND));
        });
    }

    private ScheduleResultsDTO createScheduleResults(final Long scheduleId) {
        final var results = scheduleResultsRepository.save(buildScheduleResults(scheduleId));
        return scheduleResultsMapper.buildScheduleResultsDTO(findScheduleResultsById(results.getScheduleId()));
    }

    private ScheduleResults buildScheduleResults(final Long scheduleId) {
        final var votedYes = voteRepository.countByAnswerAndScheduleId(YES, scheduleId);
        final var votedNo = voteRepository.countByAnswerAndScheduleId(NO, scheduleId);
        VoteResultEnum result;
        if (votedYes > votedNo) {
            result = VoteResultEnum.YES;
        } else {
            result = votedYes.equals(votedNo) ? VoteResultEnum.DRAW : VoteResultEnum.NO;
        }
        return ScheduleResults.builder()
                .scheduleId(scheduleId)
                .votedYes(votedYes)
                .votedNo(votedNo)
                .result(result)
                .build();
    }
}
