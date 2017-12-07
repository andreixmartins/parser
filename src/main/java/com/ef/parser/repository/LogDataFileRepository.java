package com.ef.parser.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.ef.parser.dto.InputArgumentsDTO;
import com.ef.parser.model.LogDataFile;

@Repository
public class LogDataFileRepository extends JdbcRepository {

	public void find(final InputArgumentsDTO dto) {

		RowCallbackHandler rowMapper;
		// jdbcTemplate.query("select ip from logFileData where startDate between () and ()", new Object[] {}, rowMapper);

	}

	public int[] save(final Long id, final List<LogDataFile> logs) {
		int[] value = new int[] {};
		try {
			value = this.jdbcTemplate.batchUpdate(
					"insert into logDataFile (startDate, ip, request, status, userAgent, logFileId) values (?,?,?,?,?,?)",
					new BatchPreparedStatementSetter() {
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							final LogDataFile data = logs.get(i);
							ps.setTimestamp(1, data.getStartDate());
							ps.setLong(2, data.getIp());
							ps.setString(3, data.getRequest());
							ps.setInt(4, data.getStatus());
							ps.setString(5, data.getUserAgent());
							ps.setLong(6, id);
						}

						public int getBatchSize() {
							return logs.size();
						}
					});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return value;
	}
}
