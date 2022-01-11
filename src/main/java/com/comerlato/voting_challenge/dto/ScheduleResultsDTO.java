package com.comerlato.voting_challenge.dto;

import com.comerlato.voting_challenge.enums.VoteResultEnum;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@Value
@With
@Jacksonized
@Builder
public class ScheduleResultsDTO {

    Long scheduleId;
    Long votedYes;
    Long votedNo;
    VoteResultEnum result;
}
