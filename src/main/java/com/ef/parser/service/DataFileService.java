package com.ef.parser.service;

import com.ef.parser.dto.InputArgumentsDTO;

public interface DataFileService {

	/**
	 * Main method to execute load data from log file. Checks if a given IP makes more than a certain number of requests.
	 * 
	 * @param dto
	 */
	void execute(InputArgumentsDTO dto);
}
