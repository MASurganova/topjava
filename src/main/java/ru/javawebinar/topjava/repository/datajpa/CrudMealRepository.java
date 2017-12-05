package ru.javawebinar.topjava.repository.datajpa;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Modifying
    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId " +
            "AND m.dateTime BETWEEN :startDate AND :endDate ORDER BY m.dateTime DESC")
    List<Meal> findAll(@Param("userId") int userId, @Param("startDate")LocalDateTime starDate,
                       @Param("endDate")LocalDateTime endDate);

    @Modifying
    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId " +
            "ORDER BY m.dateTime DESC")
    List<Meal> findAll(@Param("userId") int userId);

    @Modifying
    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=?1 AND m.user.id=?2")
    List<Meal> find(@Param("1") int id, @Param("2") int userId);
//    Optional<List<Meal>> findAllByUserId(int userId, Sort sort);

//    List<Meal> findAllByUserIdAndDateTimeBetween(int userId, LocalDateTime startDate, LocalDateTime endDate, Sort sort);

    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    Optional<Meal> findByIdAndUserId(int is, int userId);

    Meal save(Meal meal);

}
