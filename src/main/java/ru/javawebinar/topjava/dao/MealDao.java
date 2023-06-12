package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealDao {
    Meal get(int id);

    Collection<Meal> getAll();

    void save(Meal meal);

    void update(Meal meal, int id);

    void delete(int id);
}
