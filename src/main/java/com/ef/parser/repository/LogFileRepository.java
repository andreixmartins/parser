package com.ef.parser.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.ef.parser.model.LogFile;

@Repository
public class LogFileRepository extends JdbcRepository {

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
