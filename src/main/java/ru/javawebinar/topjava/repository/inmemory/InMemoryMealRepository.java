package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer,Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal m, Integer userId) {
        Map<Integer, Meal> meals = getMeal(userId);
        if (m.isNew()) {
            log.info("save {} for userId {}", m, userId);
            m.setId(counter.incrementAndGet());
            m.setUserId(userId);
            meals.put(m.getId(), m);
            return m;
        }
        log.info("update {} for userId {}", m, userId);
        return meals.computeIfPresent(m.getId(), (id, oldMeal) -> m);
    }

    @Override
    public boolean delete(int id, Integer userId) {
        log.info("delete {} for userId {}", id, userId);
        Map<Integer, Meal> meals = getMeal(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        log.info("get {} for userId {}", id, userId);
        Map<Integer, Meal> meals = getMeal(userId);
        return meals != null ? meals.get(id) : null;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        log.info("getAll for userId {}", userId);
        Map<Integer, Meal> meals = getMeal(userId);
        return CollectionUtils.isEmpty(meals) ? Collections.emptyList() : meals.values().stream()
                .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        log.info("getAll between dates for userId {}", userId);
        Map<Integer, Meal> meals = getMeal(userId);
        return CollectionUtils.isEmpty(meals) ?
                Collections.emptyList() :
                meals.values().stream()
                        .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(), startDateTime, endDateTime))
                        .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()))
                        .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetween(LocalDate startDate, LocalDate endDate, int userId) {
        log.info("getAll between dates for userId {}", userId);
        Map<Integer, Meal> meals = getMeal(userId);
        return CollectionUtils.isEmpty(meals) ?
                Collections.emptyList() :
                meals.values().stream()
                        .filter(m -> DateTimeUtil.isBetweenDate(m.getDate(), startDate, endDate))
                        .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                        .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getMeal(Integer userId){
        return repository.get(userId);
    }
}
