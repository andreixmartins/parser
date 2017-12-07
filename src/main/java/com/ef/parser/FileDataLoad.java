package com.ef.parser;

import java.io.File;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.ef.parser.helper.FileHelper;

public abstract class FileDataLoad {

	protected List<Path> split(final File accesslog) {
		final FileHelper fileHelper = new FileHelper();
		return fileHelper.split(accesslog);
	}

	protected Timestamp getStartDate(final String value) {
		return Timestamp.valueOf(LocalDateTime.parse(clearString(value), FileHelper.DATE_FORMATTER));
	}

	protected Integer getStatus(final String value) {
		return Integer.valueOf(clearString(value));
	}

	protected String getString(final String value) {
		return clearString(value);
	}

	protected String clearString(final String value) {
		return FileHelper.clean(value, "");
	}
}
