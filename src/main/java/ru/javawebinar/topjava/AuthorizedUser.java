package ru.javawebinar.topjava;

import java.util.HashSet;
import java.util.Set;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class AuthorizedUser {

    Set<Integer> mealsId = new HashSet<>();

    public static int id() {
        return 1;
    }

    public static int getCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public Set<Integer> getMealsId() {
        return mealsId;
    }

    public void addMeal(Integer mealId) {
        mealsId.add(mealId);
    }
}