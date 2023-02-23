package ru.javawebinar.topjava.dao;

import java.util.List;

public interface Dao<T> {
    List<T> getAll();

    T getById(int id);

    T add(T t);

    T update(T t);

    void delete(int id);
}
