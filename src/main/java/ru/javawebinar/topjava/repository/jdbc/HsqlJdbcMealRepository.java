package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
@Profile(Profiles.HSQL_DB)
public class HsqlJdbcMealRepository extends JdbcMealRepository<Timestamp> {
    public HsqlJdbcMealRepository(JdbcTemplate jt, NamedParameterJdbcTemplate npjt) {
        super(jt, npjt);
    }

    @Override
    protected Timestamp processDateTime(LocalDateTime dt) {
        return Timestamp.valueOf(dt);
    }
}
