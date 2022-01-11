package com.comerlato.voting_challenge.dto;

import com.comerlato.voting_challenge.enums.VoteAnswerEnum;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@Value
@With
@Jacksonized
@Builder
public class VoteRequestDTO {

    @NotNull
    Long scheduleId;
    @NotNull
    Long associateId;
    @NotNull
    VoteAnswerEnum answer;
}
