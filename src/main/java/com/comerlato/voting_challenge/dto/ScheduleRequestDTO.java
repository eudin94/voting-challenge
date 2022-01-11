package com.comerlato.voting_challenge.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Value
@With
@Jacksonized
@Builder
public class ScheduleRequestDTO {

    @PositiveOrZero
    Integer durationInSeconds;
    @NotNull
    String subject;
}
