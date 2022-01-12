package com.comerlato.voting_challenge.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Value
@With
@Jacksonized
@Builder
public class AssociateRequestDTO {

    @NotBlank
    String cpf;
    @NotBlank
    String name;
}
