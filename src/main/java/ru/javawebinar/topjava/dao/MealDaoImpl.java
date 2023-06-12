package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImpl implements MealDao {
    private final Map<Integer, Meal> mapMeals = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal get(int id) {
        return mapMeals.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return mapMeals.values();
    }

    @Override
    public void save(Meal m) {
        if (m.getId() == null) {
            int id = counter.incrementAndGet();
            Meal meal = new Meal(id, m.getDateTime(), m.getDescription(), m.getCalories());
            mapMeals.put(id, meal);
        }
    }

    @Override
    public void update(Meal m, int id) {
        if (mapMeals.containsKey(id)) {
            mapMeals.computeIfPresent(m.getId(), (k, v) -> m);
        } else save(m);
    }

    @Override
    public void delete(int id) {
        mapMeals.remove(id);
    }
}
