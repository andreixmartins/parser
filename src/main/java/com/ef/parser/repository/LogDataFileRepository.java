package com.ef.parser.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.ef.parser.model.LogDataFile;

@Repository
public class LogDataFileRepository extends JdbcRepository {

	public int[] save(final Long id, final List<LogDataFile> logs) {
		return this.jdbcTemplate.batchUpdate(
				"insert into logDataFile (startDate, ip, request, status, userAgent, logFileId) values (?,INET_ATON(?),?,?,?,?)",
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						final LogDataFile data = logs.get(i);
						ps.setTimestamp(1, data.getStartDate());
						ps.setString(2, data.getIp());
						ps.setString(3, data.getRequest());
						ps.setInt(4, data.getStatus());
						ps.setString(5, data.getUserAgent());
						ps.setLong(6, id);
					}

					public int getBatchSize() {
						return logs.size();
					}
				});
	}
}
