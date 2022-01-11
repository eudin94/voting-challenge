package com.comerlato.voting_challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodeEnum {

    ERROR_GENERIC_EXCEPTION("error.generic.exception"),
    ERROR_DUPLICATED_FIELD("error.duplicated.field"),
    ERROR_CPF_ALREADY_EXISTS("error.cpf.already.exists"),
    ERROR_ASSOCIATE_NOT_FOUND("error.associate.not.found"),
    ERROR_SCHEDULE_NOT_FOUND("error.schedule.not.found"),
    ERROR_CLOSED_SCHEDULE("error.closed.schedule"),
    ERROR_VOTE_ALREADY_EXISTS("error.vote.already.exists"),
    ERROR_VOTE_NOT_FOUND("error.vote.not.found");

    private final String messageKey;
}
