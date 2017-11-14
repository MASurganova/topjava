package ru.javawebinar.topjava.repository.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private UserRepository userRepository;

    public InMemoryMealRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    {
//        MealsUtil.MEALS.forEach(this::add);
//    }

    @Override
    public Meal save(Meal meal, int userId) {
        User user = userRepository.get(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            user.addMealId(meal.getId());
        }
        if (user.getMealsId().contains(meal.getId())) {
            repository.put(meal.getId(), meal);
            return meal;
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        User user = userRepository.get(userId);
        if (user.getMealsId().contains(id)) {
            user.deleteMealId(id);
            return repository.remove(id) != null;
        }
        else return false;
    }

    @Override
    public Meal get(int id, int userId) {

        Meal meal = repository.get(id);
        User user = userRepository.get(userId);
        if(meal != null && !user.getMealsId().contains(id)) return null;
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getByDate(userId, LocalDate.MIN, LocalDate.MAX);
    }

    @Override
    public List<Meal> getByDate(int userId, LocalDate start, LocalDate end) {
        User user = userRepository.get(userId);
        return repository.values().stream().filter(m -> user.getMealsId().contains(m.getId()))
                .filter(m -> DateTimeUtil.isBetween(m.getDateTime().toLocalDate(), start, end))
                .collect(Collectors.toList());
    }

}

