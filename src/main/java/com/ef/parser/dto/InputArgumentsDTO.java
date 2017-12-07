package com.ef.parser.dto;

import java.io.File;

import com.ef.parser.constant.Duration;

public class InputArgumentsDTO {

	private String startDate;

	private Duration duration;

	private int threshold;

	private File accesslog;

	public InputArgumentsDTO(String startDate, Duration duration, int threshold, File accesslog) {
		super();
		this.startDate = startDate;
		this.duration = duration;
		this.threshold = threshold;
		this.accesslog = accesslog;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public File getAccesslog() {
		return accesslog;
	}

	public void setAccesslog(File accesslog) {
		this.accesslog = accesslog;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

}
