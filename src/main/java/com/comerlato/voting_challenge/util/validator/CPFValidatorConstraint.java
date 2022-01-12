package com.comerlato.voting_challenge.util.validator;

import com.comerlato.voting_challenge.modules.integration.cpf.CPFIntegration;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hibernate.internal.util.StringHelper.isBlank;

@AllArgsConstructor
@Slf4j
public class CPFValidatorConstraint implements ConstraintValidator<CPF, String> {

    private final CPFIntegration cpfIntegration;

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext constraintValidatorContext) {
        AtomicBoolean valid = new AtomicBoolean(true);
        if (isBlank(cpf)) {
            valid.set(false);
            return valid.get();
        }
        Try.run(() -> cpfIntegration.validateCPF(cpf)).onFailure(throwable -> {
            log.error(throwable.getMessage());
            valid.set(false);
        });
        return valid.get();
    }
}
