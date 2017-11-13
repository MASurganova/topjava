package ru.javawebinar.topjava.repository.mock;

import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

//    {
//        MealsUtil.MEALS.forEach(this::add);
//    }

    @Override
    public Meal add(Meal meal, AuthorizedUser user) {
        meal.setId(counter.incrementAndGet());
        repository.put(meal.getId(), meal);
        user.addMeal(meal.getId());
        return meal;
    }

    @Override
    public Meal update(Meal meal, AuthorizedUser user) {
        if (!repository.containsKey(meal.getId()) || !user.getMealsId().contains(meal.getId())) return null;
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, AuthorizedUser user) {
        if (user.getMealsId().contains(id)) return repository.remove(id) != null;
        else return false;
    }

    @Override
    public Meal get(int id, AuthorizedUser user) {

        Meal meal = repository.get(id);
        if(meal != null && !user.getMealsId().contains(id)) return null;
        return meal;
    }

    @Override
    public List<Meal> getAll(AuthorizedUser user) {
        return getByDate(user, LocalDate.MIN, LocalDate.MAX);
    }

    public List<Meal> getByDate(AuthorizedUser user, LocalDate start, LocalDate end) {
        return repository.values().stream().filter(m -> user.getMealsId().contains(m.getId()))
                .filter(m -> DateTimeUtil.isBetween(m.getDateTime().toLocalDate(), start, end))
                .collect(Collectors.toList());
    }

}

