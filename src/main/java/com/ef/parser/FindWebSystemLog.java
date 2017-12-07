package com.ef.parser;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ef.parser.constant.Duration;
import com.ef.parser.dto.InputArgumentsDTO;
import com.ef.parser.dto.WebDataLogDTO;
import com.ef.parser.exception.ParserException;
import com.ef.parser.helper.FileHelper;
import com.ef.parser.model.LogIP;
import com.ef.parser.repository.LogIPRepository;

@Component
public class FindWebSystemLog implements SystemLog {

	private static final String MESSAGE = "IP makes more than %s requests at %s - %s.";

	private static final Logger logger = LoggerFactory.getLogger(FindWebSystemLog.class);

	@Autowired
	private LogIPRepository logIPRepository;

	@Override
	public void find(final InputArgumentsDTO dto) {

		final WebDataLogDTO dataDto = get(dto);
		print(dataDto);
		save(dataDto);
	}

	public WebDataLogDTO get(final InputArgumentsDTO dto) {

		validate(dto);

		LocalDateTime startDate;
		try {
			startDate = LocalDateTime.parse(dto.getStartDate(), FileHelper.DATE_SIMPLE_FORMATTER);
		} catch (DateTimeParseException e) {
			throw new ParserException(String.format("StartDate %s is an invalid date.", dto.getStartDate()));
		}

		final LocalDateTime endDate = dto.getDuration().equals(Duration.daily) ? startDate.plusDays(1L) : startDate.plusHours(1L);

		final List<LogIP> list = logIPRepository.find(startDate, endDate, dto.getThreshold());

		return new WebDataLogDTO(list, dto.getDuration(), dto.getThreshold(), startDate, endDate);
	}

	public void print(WebDataLogDTO dto) {

		final String message = String.format(MESSAGE, dto.getThreshold(), dto.getStartDate(), dto.getEndDate());

		if (!dto.getList().isEmpty()) {
			logger.info("IP found with threshold greate than {}:", dto.getThreshold());
		}

		dto.getList().forEach(l -> {
			l.setMessage(message);
			logger.info(l.getIp());
		});

	}

	public void save(WebDataLogDTO dto) {
		logIPRepository.save(dto.getList());
	}

	private void validate(final InputArgumentsDTO dto) {
		if (dto.getStartDate() == null) {
			throw new ParserException("StartDate param is required.");
		}

		if (dto.getThreshold() == 0) {
			throw new ParserException("Threshold param is required.");
		}

		if (dto.getDuration() == null) {
			throw new ParserException("Duration param is required.");
		}

		if (dto.getAccesslog() != null) {
			final File accesslog = new File(dto.getAccesslog());
			if (!accesslog.exists()) {
				throw new ParserException(String.format("File %s doesn't exist.", dto.getAccesslog()));
			}
		}
	}
}
