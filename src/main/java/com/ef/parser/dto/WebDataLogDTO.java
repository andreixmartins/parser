package com.ef.parser.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ef.parser.constant.Duration;
import com.ef.parser.model.LogIP;

public class WebDataLogDTO {

	private List<LogIP> list;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private Duration duration;

	private int threshold;

	public WebDataLogDTO(List<LogIP> list, Duration duration, int threshold, LocalDateTime startDate, LocalDateTime endDate) {
		this.list = list;
		this.duration = duration;
		this.threshold = threshold;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public List<LogIP> getList() {
		return list;
	}

	public void setList(List<LogIP> list) {
		this.list = list;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

}
