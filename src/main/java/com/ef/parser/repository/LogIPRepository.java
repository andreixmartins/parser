package com.ef.parser.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ef.parser.model.LogIP;

@Repository
public class LogIPRepository extends JdbcRepository {

	public List<LogIP> find(final LocalDateTime startDate, final LocalDateTime endDate, final int threshold) {

		final StringBuilder sql = new StringBuilder();
		sql.append("select INET_NTOA(ip) as ipNormal from logDataFile ");
		sql.append("where startDate BETWEEN ? and ? ");
		sql.append("group by ip ");
		sql.append("having count(ip) > ? ");

		return jdbcTemplate.query(sql.toString(), new Object[] { startDate, endDate, threshold }, logRowMapper());
	}

	private RowMapper<LogIP> logRowMapper() {
		return new RowMapper<LogIP>() {
			@Override
			public LogIP mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new LogIP(rs.getString(1));
			}
		};
	}
}
