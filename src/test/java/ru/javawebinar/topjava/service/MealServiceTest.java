package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_3;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_4;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_5;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_6;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_7;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_ID;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.getNew;
import static ru.javawebinar.topjava.MealTestData.getUpdated;
import static ru.javawebinar.topjava.MealTestData.listMeals;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(USER_MEAL_ID + 2, USER_ID);
        assertMatch(meal, USER_MEAL_3);
    }

    @Test
    public void delete() {
        mealService.delete(USER_MEAL_ID + 3, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.delete(USER_MEAL_ID + 3, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = mealService.getBetweenInclusive(LocalDate.of(2023, Month.JUNE, 21),
                LocalDate.of(2023, Month.JUNE, 21), USER_ID);
        assertMatch(meals, USER_MEAL_7, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4);
    }

    @Test
    public void getAll() {
        List<Meal> meals = mealService.getAll(USER_ID);
        assertMatch(meals, listMeals);
    }

    @Test
    public void update() {
        mealService.update(getUpdated(), USER_ID);
        assertMatch(mealService.get(USER_MEAL_ID, USER_ID), getUpdated());
    }

    @Test
    public void create() {
        Meal newMeal = mealService.create(getNew(), USER_ID);
        Integer id = newMeal.getId();
        Meal otherNewMeal = getNew();
        otherNewMeal.setId(id);

        assertMatch(newMeal, otherNewMeal);
        assertMatch(mealService.get(id, USER_ID), otherNewMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () -> mealService.create(new Meal(null,
                LocalDateTime.of(2023, Month.JUNE, 21, 13, 0), "Обед", 100),
                USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deletedOther() {
        assertThrows(NotFoundException.class, () -> mealService.delete(USER_MEAL_7.getId(), ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getOther() {
        assertThrows(NotFoundException.class, () -> mealService.get(USER_MEAL_6.getId(), ADMIN_ID));
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void updateOther() {
        assertThrows(NotFoundException.class, () -> mealService.get(USER_MEAL_5.getId(), ADMIN_ID));
    }
}