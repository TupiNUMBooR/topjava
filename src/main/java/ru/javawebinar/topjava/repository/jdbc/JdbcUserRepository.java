package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javawebinar.topjava.util.ValidationUtil.validate;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final RowMapper<Role> ROLE_ROW_MAPPER = (rs, rowNum) -> {
        var role = rs.getString("role");
        return role != null ? Role.valueOf(role) : null;
    };

    private static final ResultSetExtractor<List<User>> USER_EXTRACTOR = rs -> {
        var users = new LinkedHashMap<Integer, User>();
        while (rs.next()) {
            var id = rs.getInt("id");
            var user = users.get(id);
            if (user == null) {
                user = ROW_MAPPER.mapRow(rs, 0);
                user.setRoles(List.of());
                users.put(id, user);
            }
            var role = ROLE_ROW_MAPPER.mapRow(rs, 0);
            if (role != null) user.getRoles().add(role);
        }
        return new ArrayList<>(users.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final SimpleJdbcInsert insertRole;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.insertRole = new SimpleJdbcInsert(jdbcTemplate).withTableName("user_role");
    }

    @Override
    @Transactional
    public User save(User user) {
        validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user);
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password,
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            updateUserRoles(user);
        }

        return user;
    }

    private void updateUserRoles(User user) {
        var currentRoles = jdbcTemplate
                .queryForStream("SELECT * FROM user_role WHERE user_id=?", ROLE_ROW_MAPPER, user.getId())
                .collect(Collectors.toSet());

        var toDelete = toSqlParams(user.getId(),
                currentRoles.stream().filter(r -> !user.getRoles().contains(r)));
        namedParameterJdbcTemplate.batchUpdate("DELETE FROM user_role WHERE user_id=:user_id AND role=:role",
                toDelete);

        var toAdd = toSqlParams(user.getId(),
                user.getRoles().stream().filter(r -> !currentRoles.contains(r)));
        insertRole.executeBatch(toAdd);
    }

    private void insertRoles(User user) {
        var toAdd = toSqlParams(user.getId(), user.getRoles().stream());
        insertRole.executeBatch(toAdd);
    }

    private MapSqlParameterSource[] toSqlParams(int userId, Stream<Role> roles) {
        return roles.map(role -> new MapSqlParameterSource()
                        .addValue("user_id", userId)
                        .addValue("role", role.toString()))
                .toArray(MapSqlParameterSource[]::new);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users " +
                "LEFT JOIN user_role role ON users.id = role.user_id WHERE id=?", USER_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users " +
                "LEFT JOIN user_role role ON users.id = role.user_id WHERE email=?", USER_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users " +
                "LEFT JOIN user_role role ON users.id = role.user_id " +
                "ORDER BY name, email", USER_EXTRACTOR);
    }
}
