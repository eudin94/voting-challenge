package com.comerlato.voting_challenge.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@Value
@With
@Jacksonized
@Builder
public class ScheduleDTO {

    Long id;
    Integer durationInSeconds;
    String subject;
    Boolean closed;
}
