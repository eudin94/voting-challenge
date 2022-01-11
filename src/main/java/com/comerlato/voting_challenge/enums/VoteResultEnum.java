package com.comerlato.voting_challenge.enums;

import com.comerlato.voting_challenge.enums.serializer.EnumDescription;
import com.comerlato.voting_challenge.enums.serializer.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonSerialize(using = EnumSerializer.class)
@AllArgsConstructor
@Getter
public enum VoteResultEnum implements EnumDescription {

    YES("A maior parte dos associados votou em SIM"),
    NO("A maior parte dos associados votou em NÃO"),
    DRAW("A votação empatou");

    private final String description;
}
