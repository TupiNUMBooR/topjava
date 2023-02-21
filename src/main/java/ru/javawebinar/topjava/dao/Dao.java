package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Dao<T> {
    List<T> getAll();

    T get(int id);

    Meal add(T t);

    Meal update(T t);

    void delete(int id);
}
