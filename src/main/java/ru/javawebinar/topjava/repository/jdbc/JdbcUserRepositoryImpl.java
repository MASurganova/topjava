package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final ResultSetExtractor<List<User>> ROW_MAPPER = (ResultSetExtractor<List<User>>) rs -> {
        Map<Integer, User> map = new HashMap<Integer, User>();
        User user = null;
        Set<Role> roles = new HashSet<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            if (user != null && !user.isNew() && user.getId() != id) user.setRoles(roles);
            user = map.get(id);
            if (user == null) {
                roles = new HashSet<>();
                user = new User();
                user.setId(id);
                user.setName(rs.getString("name"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRegistered(rs.getDate("registered"));
                user.setEnabled(rs.getBoolean("enabled"));
                map.put(id, user);
            }
            String role = rs.getString("role");
            if (role != null) {
                roles.add(Role.valueOf(role));
            }
        }
        if (user != null) user.setRoles(roles);
        List<User> result = new ArrayList<User>(map.values());
        result.sort(Comparator.comparing(User::getName).thenComparing(User::getEmail));
        return result;
    };
    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            setRoles(user);
        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id",
                    parameterSource) == 0) return  null;
            deleteRoles(user);
            setRoles(user);
        }
        return user;
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id=user_roles.user_id WHERE id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id=user_roles.user_id WHERE email=?", ROW_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id=user_roles.user_id", ROW_MAPPER);
    }

    private void deleteRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());

    }

    private void setRoles(User user){
    jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
            user.getRoles(), user.getRoles().size(),
                (preparedStatement, role) -> {
                    preparedStatement.setInt(1, user.getId());
                    preparedStatement.setString(2, role.toString());
                });
    }

}
