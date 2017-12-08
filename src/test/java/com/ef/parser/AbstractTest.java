package com.ef.parser;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ef.parser.helper.FileHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public abstract class AbstractTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	protected Path createTempFile() {

		Path createTempFile = null;
		try {
			createTempFile = Files.createTempFile("log-test", ".log");
			final FileOutputStream fileOutPutStream = new FileOutputStream(createTempFile.toFile());
			try (final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutPutStream)) {
				bufferedOutputStream.write("2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"".getBytes());
				bufferedOutputStream.write(FileHelper.BREAK_LINE.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createTempFile;
	}
}
