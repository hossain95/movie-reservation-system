package com.mrs.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private static final String BD_PHONE_REGEX = "^(?:\\+8801[3-9]\\d{8}|01[3-9]\\d{8})$";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if(phoneNumber == null) return false;
        return phoneNumber.matches(BD_PHONE_REGEX);
    }
}
