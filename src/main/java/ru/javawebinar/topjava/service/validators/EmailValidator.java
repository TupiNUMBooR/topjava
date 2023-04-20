package ru.javawebinar.topjava.service.validators;

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
        // @idAndEmail обязан содержать id для корректной проверки на сохранение без изменения своего емейла
        // Тоесть проверку можно делать только после assureIdConsistent, поэтому я делаю ее в сервисах
        var idAndEmail = (HasIdAndEmail) target;
        var email = idAndEmail.getEmail().toLowerCase();
        var foundByEmail = repository.getByEmail(email);

        if (foundByEmail == null) {
            return;
        }

        if (!idAndEmail.isNew() && foundByEmail.getId().equals(idAndEmail.getId())) {
            // если email того же пользователя, которого изменяем
            return;
        }

        errors.rejectValue("email", "user.emailExists", new Object[]{email}, "");
    }
}
