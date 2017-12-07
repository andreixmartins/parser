package com.ef.parser.model;

public class LogIP {

	private String ip;

	private String message;

	public LogIP(String ip, String message) {
		this.ip = ip;
		this.message = message;
	}

	public LogIP(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
