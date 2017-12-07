package com.ef.parser.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

import com.ef.parser.constant.Duration;
import com.ef.parser.dto.InputArgumentsDTO;
import com.ef.parser.exception.ParserException;
import com.ef.parser.helper.FileHelper;
import com.ef.parser.model.LogDataFile;
import com.ef.parser.model.LogFile;
import com.ef.parser.model.LogIP;
import com.ef.parser.repository.LogDataFileRepository;
import com.ef.parser.repository.LogFileRepository;
import com.ef.parser.repository.LogIPRepository;

@Service
public class DataFileServiceImpl implements DataFileService {

	private static final Logger logger = LoggerFactory.getLogger(DataFileServiceImpl.class);

	@Autowired
	private LogDataFileRepository logDataFileRepository;

	@Autowired
	private LogFileRepository logFileRepository;

	@Autowired
	private LogIPRepository logIPRepository;

	private List<LogDataFile> logs = null;

	@Override
	public void execute(final InputArgumentsDTO dto) {

		if (dto.getAccesslog() != null) {
			try {
				load(dto);
			} catch (IOException e) {
				logger.error("Errors occured on load data file {}", dto.getAccesslog());
			}
		}

		find(dto);
	}

	public void find(final InputArgumentsDTO dto) {

		if (dto.getStartDate() == null) {
			throw new ParserException("StartDate param is required.");
		}

		if (dto.getThreshold() == 0) {
			throw new ParserException("Threshold param is required.");
		}

		if (dto.getDuration() == null) {
			throw new ParserException("Duration param is required.");
		}

		final LocalDateTime startDate = LocalDateTime.parse(dto.getStartDate(), FileHelper.DATE_SIMPLE_FORMATTER);
		final LocalDateTime endDate = dto.getDuration().equals(Duration.daily) ? startDate.plusDays(1L) : startDate.plusHours(1L);

		final List<LogIP> blockedIps = logIPRepository.find(startDate, endDate, dto.getThreshold());

		logIPInfo(blockedIps, dto.getDuration(), dto.getThreshold(), startDate, endDate);
	}

	private void logIPInfo(List<LogIP> blockedIps, Duration duration, int threshold, LocalDateTime startDate, LocalDateTime endDate) {
		final String message = "IP %s makes more than %s requests at %s - %s.";
		blockedIps.forEach(l -> {
			logger.info(String.format(message, l.getIp(), threshold, startDate, endDate));
		});
	}

	private void load(final InputArgumentsDTO dto) throws IOException {

		if (dto.getAccesslog() == null) {
			throw new ParserException("Accesslog param is required.");
		}

		final File accesslog = new File(dto.getAccesslog());

		logger.info("Loading file {} to database", accesslog.getName());

		final FileHelper fileHelper = new FileHelper();
		final List<Path> files = fileHelper.split(accesslog);

		if (!files.isEmpty()) {

			final LogFile logFile = new LogFile(accesslog.getName());
			logFileRepository.save(logFile);

			files.forEach(file -> {
				try (final BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
					logs = br.lines().map(mapData).collect(Collectors.toList());
				} catch (IOException e) {
					String message = "Error occured when import data";
					logger.error(message, e);
					throw new ParserException(message);
				}
				logDataFileRepository.save(logFile.getId(), logs);
			});
		}

		logger.info("The file {} was imported with success", accesslog.getName());
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
				getString(data[1]),
				getString(data[2]),
				getStatus(data[3]),
				getString(data[4]));
	}

	private Timestamp getStartDate(final String value) {
		return Timestamp.valueOf(LocalDateTime.parse(clearString(value), FileHelper.DATE_FORMATTER));
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
