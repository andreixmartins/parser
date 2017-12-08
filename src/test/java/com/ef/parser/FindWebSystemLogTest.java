package com.ef.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.ef.parser.constant.Duration;
import com.ef.parser.dto.InputArgumentsDTO;
import com.ef.parser.exception.ParserException;
import com.ef.parser.model.LogIP;
import com.ef.parser.repository.LogIPRepository;

@RunWith(MockitoJUnitRunner.class)
public class FindWebSystemLogTest extends AbstractTest {

	private SystemLog systemLog;

	@Mock
	private LogIPRepository logIPRepository;

	@Before
	public void setup() {
		systemLog = new FindWebSystemLog();
		ReflectionTestUtils.setField(systemLog, "logIPRepository", logIPRepository);
	}

	@Test(expected = ParserException.class)
	public void mustThrownExceptionWhenDateStartIsNull() {
		InputArgumentsDTO dto = new InputArgumentsDTO(null, Duration.daily, 500, null);
		systemLog.find(dto);
	}

	@Test(expected = ParserException.class)
	public void mustThrownExceptionWhenThresholdIsZero() {
		InputArgumentsDTO dto = new InputArgumentsDTO("2017-01-01.13:00:00", Duration.daily, 0, null);
		systemLog.find(dto);
	}

	@Test(expected = ParserException.class)
	public void mustThrownExceptionWhenDurationIsNull() {
		InputArgumentsDTO dto = new InputArgumentsDTO("2017-01-01.13:00:00", null, 100, null);
		systemLog.find(dto);
	}

	@Test(expected = ParserException.class)
	public void mustThrownExceptionWhenDateStartIsInvalid() {
		InputArgumentsDTO dto = new InputArgumentsDTO("2017-01-01", Duration.daily, 100, null);
		systemLog.find(dto);
	}

	@Test
	public void mustLoadData() {

		final List<LogIP> list = new ArrayList<>();
		list.add(new LogIP("127.0.0.1"));
		list.add(new LogIP("127.0.0.2"));
		list.add(new LogIP("127.0.0.3"));

		Mockito.when(logIPRepository.find(LocalDateTime.now(), LocalDateTime.now(), 2)).thenReturn(list);

		InputArgumentsDTO dto = new InputArgumentsDTO("2017-01-01.13:00:00", Duration.daily, 100, null);
		systemLog.find(dto);
	}
}
