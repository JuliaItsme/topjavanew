package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID = START_SEQ + 3;
    public static final int ADMIN_MEAL_ID = START_SEQ + 10;
    public static final int NOT_FOUND = 10;

    public final static Meal USER_MEAL_1 = new Meal(USER_MEAL_ID,
            LocalDateTime.of(2023, Month.JUNE, 20, 10, 0), "Завтрак", 500);
    public final static Meal USER_MEAL_2 = new Meal(USER_MEAL_ID + 1,
            LocalDateTime.of(2023, Month.JUNE, 20, 13, 0), "Обед", 1000);
    public final static Meal USER_MEAL_3 = new Meal(USER_MEAL_ID + 2,
            LocalDateTime.of(2023, Month.JUNE, 20, 20, 0), "Ужин", 500);
    public final static Meal USER_MEAL_4 = new Meal(USER_MEAL_ID + 3,
            LocalDateTime.of(2023, Month.JUNE, 21, 0, 0),
            "Еда на граничное значение", 100);
    public final static Meal USER_MEAL_5 = new Meal(USER_MEAL_ID + 4,
            LocalDateTime.of(2023, Month.JUNE, 21, 10, 0), "Завтрак", 500);
    public final static Meal USER_MEAL_6 = new Meal(USER_MEAL_ID + 5,
            LocalDateTime.of(2023, Month.JUNE, 21, 13, 0), "Обед", 1000);
    public final static Meal USER_MEAL_7 = new Meal(USER_MEAL_ID + 6,
            LocalDateTime.of(2023, Month.JUNE, 21, 20, 0), "Ужин", 510);

    public final static Meal ADMIN_MEAL_1 = new Meal(ADMIN_MEAL_ID,
            LocalDateTime.of(2023, Month.JUNE, 21, 14, 0), "Админ ланч", 510);
    public final static Meal ADMIN_MEAL_2 = new Meal(ADMIN_MEAL_ID + 1,
            LocalDateTime.of(2023, Month.JUNE, 21, 21, 0), "Админ ужин", 1500);

    public final static List<Meal> listMeals = Arrays.asList(USER_MEAL_7, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4,
            USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2023, Month.JUNE, 22, 22, 0),
                "ужин", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(USER_MEAL_ID, USER_MEAL_1.getDateTime(), "Новый завтрак", 500);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
