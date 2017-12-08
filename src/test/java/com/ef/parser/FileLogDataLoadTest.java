package com.ef.parser;

import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.ef.parser.constant.Duration;
import com.ef.parser.dto.InputArgumentsDTO;
import com.ef.parser.exception.ParserException;
import com.ef.parser.repository.LogDataFileRepository;
import com.ef.parser.repository.LogFileRepository;

@RunWith(MockitoJUnitRunner.class)
public class FileLogDataLoadTest extends AbstractTest {

	private DataLoad dataLog;

	@Mock
	private LogDataFileRepository logDataFileRepository;

	@Mock
	private LogFileRepository logFileRepository;

	@Before
	public void setup() {
		dataLog = new FileLogDataLoad();
		ReflectionTestUtils.setField(dataLog, "logDataFileRepository", logDataFileRepository);
		ReflectionTestUtils.setField(dataLog, "logFileRepository", logFileRepository);
	}

	@Test
	public void loadTest() {
		Path tempFile = createTempFile();
		InputArgumentsDTO dto = new InputArgumentsDTO("2017-01-01.13:00:00", Duration.daily, 500, tempFile.toAbsolutePath().toString());
		dataLog.load(dto);
	}

	@Test(expected = ParserException.class)
	public void mustThrownWhenLogFileNotExist() {
		InputArgumentsDTO dto = new InputArgumentsDTO("2017-01-01.13:00:00", Duration.daily, 500, "notFoundFile.log");
		dataLog.load(dto);
	}

	@Test(expected = ParserException.class)
	public void mustThrownWhenLogFileIsEmpty() {
		InputArgumentsDTO dto = new InputArgumentsDTO("2017-01-01.13:00:00", Duration.daily, 500, "");
		dataLog.load(dto);
	}

}
