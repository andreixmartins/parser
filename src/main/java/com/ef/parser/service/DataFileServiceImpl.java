package com.ef.parser.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ef.parser.dto.InputArgumentsDTO;
import com.ef.parser.exception.ParserException;
import com.ef.parser.helper.FileHelper;
import com.ef.parser.model.LogDataFile;
import com.ef.parser.model.LogFile;
import com.ef.parser.repository.DataFileRepository;
import com.ef.parser.repository.LogDataFileRepository;
import com.ef.parser.repository.LogFileRepository;

@Service
public class DataFileServiceImpl implements DataFileService {

	private static final Logger logger = LoggerFactory.getLogger(DataFileServiceImpl.class);

	@Autowired
	private DataFileRepository dataFileRepository;

	@Autowired
	private LogDataFileRepository logDataFileRepository;

	@Autowired
	private LogFileRepository logFileRepository;

	private List<LogDataFile> logs = null;

	@Override
	public void importData(final InputArgumentsDTO dto) throws IOException {

		logger.info("Loading file {} to database", dto.getAccesslog().getName());

		final FileHelper fileHelper = new FileHelper();
		final List<Path> files = fileHelper.split(dto.getAccesslog());

		if (!files.isEmpty()) {

			final LogFile logFile = new LogFile(dto.getAccesslog().getName());
			logFileRepository.save(logFile);

			files.forEach(file -> {
				try (final BufferedReader br = dataFileRepository.load(file)) {
					logs = br.lines().map(mapData).collect(Collectors.toList());
				} catch (IOException e) {
					String message = "Error occured when import data";
					logger.error(message, e);
					throw new ParserException(message);
				}
				logDataFileRepository.save(logFile.getId(), logs);
			});
		}

		logger.info("The file {} was imported with success", dto.getAccesslog().getName());
	}

	private Function<String, LogDataFile> mapData = (line) -> {
		return buildLogDataFile(line.split(FileHelper.PIPE));
	};

	private LogDataFile buildLogDataFile(final String[] data) {

		if (data.length != 5) {
			logger.warn("Invalid data");
		}

		return new LogDataFile(
				getStartDate(data[0]),
				getIP(data[1]),
				getString(data[2]),
				getStatus(data[3]),
				getString(data[4]));
	}

	private Timestamp getStartDate(final String value) {
		return Timestamp.valueOf(LocalDateTime.parse(clearString(value), FileHelper.DATE_FORMATTER));
	}

	private Long getIP(final String value) {
		return Long.valueOf(FileHelper.clean(clearString(value), FileHelper.DOT));
	}

	private Integer getStatus(final String value) {
		return Integer.valueOf(clearString(value));
	}

	private String getString(final String value) {
		return clearString(value);
	}

	private String clearString(final String value) {
		return FileHelper.clean(value, "");
	}

}
