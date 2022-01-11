package com.comerlato.voting_challenge.util.mapper;

import com.comerlato.voting_challenge.dto.VoteDTO;
import com.comerlato.voting_challenge.dto.VoteRequestDTO;
import com.comerlato.voting_challenge.modules.entity.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VoteMapper {

    @Mapping(target = "id", ignore = true)
    Vote buildVote(VoteRequestDTO request);

    VoteDTO buildVoteDTO(Vote vote);
}
