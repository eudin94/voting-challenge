package com.comerlato.voting_challenge.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@Value
@With
@Jacksonized
@Builder
public class AssociateRequestDTO {

    //TODO Criar anotação com integração para verificar se o CPF é válido
    String cpf;
    String name;
}
