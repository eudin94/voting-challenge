package com.comerlato.voting_challenge.enums;

import com.comerlato.voting_challenge.enums.serializer.EnumDescription;
import com.comerlato.voting_challenge.enums.serializer.EnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonSerialize(using = EnumSerializer.class)
@AllArgsConstructor
@Getter
public enum VoteAnswerEnum implements EnumDescription {

    YES("Sim"),
    NO("Não");

    private final String description;
}
