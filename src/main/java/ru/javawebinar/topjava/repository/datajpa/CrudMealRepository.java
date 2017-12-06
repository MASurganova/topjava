package ru.javawebinar.topjava.repository.datajpa;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    Optional<Meal> findByIdAndUserId(int is, int userId);

    List<Meal> findAllByUserIdAndDateTimeBetween(int userId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Meal> findAllByUserId(int userId, Sort sort);

    Meal save(Meal meal);

    @EntityGraph(attributePaths = "user")
    Optional<Meal> findByUserIdAndId(int userId, int Id);

}
