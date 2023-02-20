package ru.javawebinar.topjava.dao;

import java.util.List;

public interface IDao<T> {
    List<T> getAll();

    T getById(int id);

    void add(T t);

    void update(T t);

    void delete(int id);
}
