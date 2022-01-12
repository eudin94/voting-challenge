package com.comerlato.voting_challenge.dto;

import com.comerlato.voting_challenge.util.validator.ValidCPF;
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

    @ValidCPF
    String cpf;
    @NotBlank
    String name;
}
