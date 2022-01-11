package com.comerlato.voting_challenge.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@Value
@With
@Jacksonized
@Builder
public class ScheduleRequestDTO {

    Integer durationInSeconds;
    @NotNull
    String subject;
}
