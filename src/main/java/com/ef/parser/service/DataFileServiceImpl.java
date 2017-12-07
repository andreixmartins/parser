package com.ef.parser.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ef.parser.DataLoad;
import com.ef.parser.SystemLog;
import com.ef.parser.dto.InputArgumentsDTO;
import com.ef.parser.exception.ParserException;

@Service
public class DataFileServiceImpl implements DataFileService {

	private static final Logger logger = LoggerFactory.getLogger(DataFileServiceImpl.class);

	@Autowired
	private SystemLog systemLog;

	@Autowired
	private DataLoad dataLoad;

	@Override
	public void execute(final InputArgumentsDTO dto) {

		try {
			dataLoad.load(dto);
			systemLog.find(dto);
		} catch (ParserException e) {
			logger.error("Errors occured on load data file {} - Message: {}", dto.getAccesslog(), e.getMessage());
		}
	}
}
