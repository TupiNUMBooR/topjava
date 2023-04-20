package ru.javawebinar.topjava.service.validators;

import org.springframework.validation.BindingResult;

public class DuplicateEmailException extends RuntimeException {
    public final BindingResult bindingResult;

    public DuplicateEmailException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
