package com.ef.parser.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.ef.parser.model.LogFile;

@Repository
public class LogFileRepository extends JdbcRepository {

	public LogFile findByName(final String name) {
		try {
			return jdbcTemplate.queryForObject("select name from logFile where name = ?", new Object[] { name }, logRowMapper());
		} catch (EmptyResultDataAccessException e) {
		}
		return null;
	}

	private RowMapper<LogFile> logRowMapper() {
		return new RowMapper<LogFile>() {
			@Override
			public LogFile mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new LogFile(rs.getString(1));
			}
		};
	}

	public void save(final LogFile logFile) {
		final GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				final PreparedStatement statement = con.prepareStatement("insert into logFile (name) values (?)", Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, logFile.getName());
				return statement;
			}
		}, holder);

		logFile.setId(holder.getKey().longValue());
	}
}
