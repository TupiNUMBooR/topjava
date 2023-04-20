package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.HasId;

public interface HasIdAndEmail extends HasId {
    String getEmail();
}
