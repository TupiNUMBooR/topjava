package ru.javawebinar.topjava.web.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.HasIdAndEmail;
import ru.javawebinar.topjava.repository.UserRepository;

@Component
public class EmailValidator implements Validator {

    @Autowired
    UserRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var idAndEmail = (HasIdAndEmail) target;

        if (idAndEmail.getEmail() == null) {
            return;
        }

        var email = idAndEmail.getEmail().toLowerCase();
        var foundByEmail = repository.getByEmail(email);

        if (foundByEmail != null && !foundByEmail.getId().equals(idAndEmail.getId())) {
            errors.rejectValue("email", "user.emailExists", new Object[]{email}, "");
        }
    }
}
