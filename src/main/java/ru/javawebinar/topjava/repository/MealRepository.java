package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.List;

public interface MealRepository {
    Meal add(Meal meal, AuthorizedUser user);

    Meal update(Meal meal, AuthorizedUser user);

    boolean delete(int id, AuthorizedUser user);

    Meal get(int id, AuthorizedUser user);

    List<Meal> getAll(AuthorizedUser user);
}
