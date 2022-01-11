package com.comerlato.voting_challenge.dto;

import com.comerlato.voting_challenge.enums.VoteAnswerEnum;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@Value
@With
@Jacksonized
@Builder
public class VoteDTO {

    Long id;
    Long scheduleId;
    Long associateId;
    VoteAnswerEnum answer;
}
