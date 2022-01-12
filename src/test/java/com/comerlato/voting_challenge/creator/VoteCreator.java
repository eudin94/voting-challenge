package com.comerlato.voting_challenge.creator;

import com.comerlato.voting_challenge.dto.VoteDTO;
import com.comerlato.voting_challenge.dto.VoteRequestDTO;
import com.comerlato.voting_challenge.modules.entity.Vote;

import static com.comerlato.voting_challenge.creator.AssociateCreator.associate;
import static com.comerlato.voting_challenge.creator.ScheduleCreator.schedule;
import static com.comerlato.voting_challenge.enums.VoteAnswerEnum.YES;
import static com.comerlato.voting_challenge.util.mapper.MapperConstants.voteMapper;

public class VoteCreator {

    public static final VoteRequestDTO voteRequestDTO = createVoteRequest();
    public static final Vote vote = voteMapper.buildVote(voteRequestDTO).withId(1L);
    public static final VoteDTO voteDTO = voteMapper.buildVoteDTO(vote);

    private static VoteRequestDTO createVoteRequest() {
        return VoteRequestDTO.builder()
                .scheduleId(schedule.getId())
                .associateId(associate.getId())
                .answer(YES)
                .build();
    }
}
