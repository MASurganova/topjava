package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Modifying
    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC")
    List<Meal> findAll(@Param("userId") int userId);


    @Modifying
    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId " +
            "AND m.dateTime BETWEEN :startDate AND :endDate ORDER BY m.dateTime DESC")
    List<Meal> findAll(@Param("userId") int userId, @Param("startDate")LocalDateTime starDate,
                       @Param("endDate")LocalDateTime endDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param("id")int id, @Param("userId") int userId);

    @Override
    Optional<Meal> findById(Integer integer);

    @Override
    Meal save(Meal meal);
}
