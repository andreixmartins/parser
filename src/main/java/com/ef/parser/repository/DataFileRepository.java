package com.ef.parser.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class DataFileRepository {

	@Autowired
	private ResourceLoader resourceLoader;

	public BufferedReader load(final Path file) throws IOException {
		// return new BufferedReader(new InputStreamReader(new FileInputStream(file.toFile())));
		return new BufferedReader(new FileReader(file.toFile()));
	}
}
