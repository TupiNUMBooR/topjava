package ru.javawebinar.topjava.util.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateEmailException extends DataIntegrityViolationException {
    public DuplicateEmailException(String msg) {
        super(msg);
    }
}