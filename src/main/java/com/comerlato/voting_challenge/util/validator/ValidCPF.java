package com.comerlato.voting_challenge.util.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CPFValidatorConstraint.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCPF {
    String message() default "É preciso informar um CPF válido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}