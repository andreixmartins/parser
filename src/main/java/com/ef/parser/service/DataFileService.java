package com.ef.parser.service;

import java.io.IOException;

import com.ef.parser.dto.InputArgumentsDTO;

public interface DataFileService {

	void importData(final InputArgumentsDTO dto) throws IOException;
}
