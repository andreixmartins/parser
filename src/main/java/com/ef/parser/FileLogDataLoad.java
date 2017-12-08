package com.ef.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ef.parser.dto.InputArgumentsDTO;
import com.ef.parser.exception.ParserException;
import com.ef.parser.helper.FileHelper;
import com.ef.parser.model.LogDataFile;
import com.ef.parser.model.LogFile;
import com.ef.parser.repository.LogDataFileRepository;
import com.ef.parser.repository.LogFileRepository;

@Component
public class FileLogDataLoad extends FileDataLoad implements DataLoad {

	private static final Logger logger = LoggerFactory.getLogger(FileLogDataLoad.class);

	@Autowired
	private LogDataFileRepository logDataFileRepository;

	@Autowired
	private LogFileRepository logFileRepository;

	private List<LogDataFile> logs = null;

	@Override
	public void load(final InputArgumentsDTO dto) {

		if (dto.getAccesslog() != null) {

			validade(dto);

			final File accesslog = new File(dto.getAccesslog());

			logger.info("Loading file {} to database", accesslog.getName());

			final List<Path> files = split(accesslog);

			if (!files.isEmpty()) {

				final LogFile logFile = new LogFile(accesslog.getName());
				logFileRepository.save(logFile);
				generateDataLog(logFile.getId(), files);
			}

			logger.info("The file {} was imported with success", accesslog.getName());
		}
	}

	private void generateDataLog(final Long id, final List<Path> files) {
		files.forEach(file -> {
			final List<LogDataFile> logs = build(file);
			save(id, logs);
		});
	}

	private void save(final Long id, final List<LogDataFile> logs) {
		logDataFileRepository.save(id, logs);
	}

	private List<LogDataFile> build(Path file) {
		try (final BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
			logs = br.lines().map(mapData).collect(Collectors.toList());
		} catch (IOException e) {
			String message = "Error occured when was import data";
			logger.error(message, e);
			throw new ParserException(message);
		}
		return logs;
	}

	private void validade(final InputArgumentsDTO dto) {

		final File accesslog = new File(dto.getAccesslog());
		LogFile logFile = logFileRepository.findByName(accesslog.getName());

		if (logFile != null) {
			throw new ParserException(String.format("File %s already imported.", dto.getAccesslog()));
		}

		if (!accesslog.exists()) {
			throw new ParserException(String.format("File %s doesn't exist.", dto.getAccesslog()));
		}
	}

	private Function<String, LogDataFile> mapData = (line) -> {
		return buildLogDataFile(line.split(FileHelper.PIPE));
	};

	private LogDataFile buildLogDataFile(final String[] data) {
		if (data.length != FileHelper.DEFAULT_COLUMN_LENGTH) {
			throw new ParserException("File contains invalid data.");
		}
		return new LogDataFile(getStartDate(data[0]), getString(data[1]), getString(data[2]), getStatus(data[3]), getString(data[4]));
	}
}
