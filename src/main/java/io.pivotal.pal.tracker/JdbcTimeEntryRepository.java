package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
public class JdbcTimeEntryRepository implements TimeEntryRepository {




    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.dataSource = dataSource;

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry any) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours) " +
                    "VALUES (?, ?, ?, ?)", RETURN_GENERATED_KEYS);
            statement.setLong(1, any.getProjectId());
            statement.setLong(2, any.getUserId());
            statement.setDate(3, Date.valueOf(any.getDate()));
            statement.setInt(4, any.getHours());
            return statement;
        }, generatedKeyHolder);


        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        String qry = "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?";
        return jdbcTemplate.query(qry,new Object[]{timeEntryId},extractor);


    }
    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );
    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;

    @Override
    public List<TimeEntry> list() {
        String qry ="SELECT id, project_id, user_id, date, hours FROM time_entries";
        return jdbcTemplate.query(qry,mapper);
    }

    @Override
    public TimeEntry update(long eq, TimeEntry any) {
        String qry ="UPDATE time_entries " +
                "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(qry,
                any.getProjectId(),
                any.getUserId(),
                Date.valueOf(any.getDate()),
                any.getHours(),
                eq);

        return find(eq);
    }

    @Override
    public List<TimeEntry> delete(long timeEntryId) {
        String qry = "DELETE FROM time_entries WHERE ID =?";
        jdbcTemplate.update(qry,new Object[]{timeEntryId});
        return list();
    }

    @Override
    public List getList() {
        return null;
    }
}
