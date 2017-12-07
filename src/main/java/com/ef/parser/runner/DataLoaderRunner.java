package com.ef.parser.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ef.parser.constant.Duration;
import com.ef.parser.dto.InputArgumentsDTO;
import com.ef.parser.service.DataFileService;

@Component
@Order(1)
public class DataLoaderRunner implements ApplicationRunner {

	@Value("${startDate}")
	private String startDate;

	@Value("${duration:hourly}")
	private String duration;

	@Value("${threshold}")
	private int threshold;

	@Value("${accesslog:#{null}}")
	private String accesslog;

	@Autowired
	DataFileService dataFileService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		final InputArgumentsDTO dto = new InputArgumentsDTO(startDate, Duration.valueOf(duration), threshold, accesslog);
		dataFileService.execute(dto);
	}
}
