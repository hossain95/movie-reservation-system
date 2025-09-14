package com.mrs.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Invalid password. Must contain at least 8 characters, uppercase, lowercase, number, and special character.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
