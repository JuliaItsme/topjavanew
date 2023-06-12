package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int CALORIES_PER_DAY = 2000;
    public static final List<Meal> meals = new ArrayList<>();

    static {
        meals.add(new Meal(LocalDateTime
                .of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(LocalDateTime
                .of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(LocalDateTime
                .of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(LocalDateTime
                .of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.add(new Meal(LocalDateTime
                .of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(LocalDateTime
                .of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(LocalDateTime
                .of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    public static List<MealTo> getMealTo(Collection<Meal> meals, int caloriesPerDay) {
        return filtered(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getMealToWithFilter
            (Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filtered(meals, caloriesPerDay, meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    private static List<MealTo> filtered(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> mealFilter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream().collect(Collectors.groupingBy(Meal::getDate,
                Collectors.summingInt(Meal::getCalories))
        );

        return meals.stream()
                .filter(mealFilter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    private static List<MealTo> filteredByStreams
            (List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
        );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > CALORIES_PER_DAY))
                .collect(Collectors.toList());
    }
}
