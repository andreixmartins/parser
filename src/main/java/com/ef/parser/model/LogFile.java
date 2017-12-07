package com.ef.parser.model;

import java.time.LocalDateTime;

public class LogFile {

	private Long id;

	private String name;

	private LocalDateTime startDate;

	public LogFile(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getStartDate() {
		if (startDate == null) {
			startDate = LocalDateTime.now();
		}
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

}
