# parser


Sample commands

	- java -jar "parser.jar" --startDate=2017-01-01.13:00:00 --duration=daily --threshold=250
	- java -jar "parser.jar" --startDate=2017-01-01.13:00:00 --duration=daily --threshold=250 --accesslog=/path/to/file
	

Docker MySQL

	docker run --name parser -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=parser -d mysql:latest

MySQL connection
	
	- jdbc:mysql://localhost:3306/parser
	
MySQL user

	user: root
	password: 123456
	
	
Queries used

	select INET_NTOA(ip) from logDataFile 
	where startDate BETWEEN '2017-01-01 00:00:00' and '2017-01-01.14:00:00'
	group by ip
	having count(ip) > 100
	
	
	
Schema SQL



