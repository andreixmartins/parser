


CREATE TABLE IF NOT EXISTS logFile (
	id SMALLINT PRIMARY KEY AUTO_INCREMENT,
	startDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS logDataFile (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	startDate DATETIME NOT NULL,
	ip BIGINT UNSIGNED NOT NULL,
	request VARCHAR(20) NOT NULL,
	status SMALLINT UNSIGNED NOT NULL,
	userAgent VARCHAR(255) NOT NULL,
	logFileId SMALLINT NOT NULL,
	CONSTRAINT FOREIGN KEY FK_LOG_FILE (logFileId) REFERENCES logFile(id)
);