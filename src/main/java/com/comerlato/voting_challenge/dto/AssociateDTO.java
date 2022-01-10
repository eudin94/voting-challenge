package com.comerlato.voting_challenge.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@Value
@With
@Jacksonized
@Builder
public class AssociateDTO {

    Long id;
    String cpf;
    String name;
}
