package com.ef.parser.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FileHelper {

	public static final String DOT = "\\.";

	public static final String QUOTE = "\"";

	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.US);

	private static final String BREAK_LINE = "\r\n";

	public static final String PIPE = "\\|";

	private static final int MAX_SPLITS = 10;

	public List<Path> split(final File file) {

		final List<Path> files = new ArrayList<Path>();

		try (final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {

			long sourceSize = randomAccessFile.length();
			long bytesPerSplit = sourceSize / MAX_SPLITS;

			split: for (int i = 1; i <= MAX_SPLITS; i++) {
				Path createTempFile = Files.createTempFile("log-split-", i + ".log");
				files.add(createTempFile);

				final FileOutputStream fileOutPutStream = new FileOutputStream(createTempFile.toFile());
				try (final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutPutStream)) {

					String line = null;
					while ((line = randomAccessFile.readLine()) != null) {
						if (createTempFile.toFile().length() > bytesPerSplit) {
							continue split;
						}

						bufferedOutputStream.write(line.getBytes());
						bufferedOutputStream.write(BREAK_LINE.getBytes());
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return files;
	}

	public static String clean(final String value, final String replace) {
		if (value == null) {
			return "";
		}
		return value.replaceAll(replace, "");
	}
}
