package com.comerlato.voting_challenge.service;

import com.comerlato.voting_challenge.dto.ScheduleDTO;
import com.comerlato.voting_challenge.dto.ScheduleRequestDTO;
import com.comerlato.voting_challenge.dto.VoteDTO;
import com.comerlato.voting_challenge.dto.VoteRequestDTO;
import com.comerlato.voting_challenge.entity.Schedule;
import com.comerlato.voting_challenge.entity.Vote;
import com.comerlato.voting_challenge.helper.MessageHelper;
import com.comerlato.voting_challenge.repository.ScheduleRepository;
import com.comerlato.voting_challenge.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.comerlato.voting_challenge.exception.ErrorCodeEnum.*;
import static com.comerlato.voting_challenge.util.mapper.MapperConstants.scheduleMapper;
import static com.comerlato.voting_challenge.util.mapper.MapperConstants.voteMapper;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final AssociateService associateService;
    private final ScheduleRepository repository;
    private final VoteRepository voteRepository;
    private final MessageHelper messageHelper;

    public ScheduleDTO create(final ScheduleRequestDTO request) {
        final var savedSchedule = repository.save(scheduleMapper.buildSchedule(request).withClosed(false)
                .withDurationInSeconds(nonNull(request.getDurationInSeconds()) ? request.getDurationInSeconds() : 60));
        return findDTOById(savedSchedule.getId());
    }

    public ScheduleDTO findDTOById(final Long id) {
        return scheduleMapper.buildScheduleDTO(findById(id));
    }

    public ScheduleDTO closeSchedule(final Long id) {
        final var schedule = findById(id);
        final var closedSchedule = repository.save(schedule.withClosed(true));
        return findDTOById(closedSchedule.getId());
    }

    public VoteDTO voteForSchedule(final VoteRequestDTO request) {
        validateVote(request.getScheduleId(), request.getAssociateId());
        final var savedVote = voteRepository.save(voteMapper.buildVote(request));
        return findVoteDTOById(savedVote.getId());
    }

    public VoteDTO findVoteDTOById(final Long voteId) {
        return voteMapper.buildVoteDTO(findVoteById(voteId));
    }

    private void validateVote(final Long scheduleId, final Long associateId) {
        final var schedule = findDTOById(scheduleId);
        if (schedule.getClosed())
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_CLOSED_SCHEDULE));
        final var associate = associateService.findDTOById(associateId);
        final var existingVote = voteRepository
                .findByScheduleIdAndAssociateId(schedule.getId(), associate.getId());
        if (existingVote.isPresent())
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_VOTE_ALREADY_EXISTS));
    }

    private Schedule findById(final Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                messageHelper.get(ERROR_SCHEDULE_NOT_FOUND)));
    }

    private Vote findVoteById(final Long voteId) {
        return voteRepository.findById(voteId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                messageHelper.get(ERROR_VOTE_NOT_FOUND)));
    }
}
