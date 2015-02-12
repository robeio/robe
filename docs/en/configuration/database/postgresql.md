##USING ROBE WITH POSTGRESQL
###Pre-requirement
PostgreSQL JDBC driver has to be downloaded from [here](https://jdbc.postgresql.org/download.html). Please find your PostgreSQL Driver version from the list. Import the jar file to your own project or you can import maven dependency from [here](http://mvnrepository.com/artifact/org.postgresql/postgresql).

###Configuration
1. Open robe.yml file and find **hibernate** configuration header.
2. Change `driverClass` parameter which under the *database* sub-parameter described below;

		driverClass: org.postgresql.Driver

3. Fill `user`,`password` and `url` eg:

		user: dbUser
		password: dbPassword 
		url: jdbc:postgresql://IPADDRESS:PORT/tablename

3. Change `dialect` parameter which under the *properties* sub-parameter described below;

		dialect: org.hibernate.dialect.PostgreSQLDialect
		
###Common Problems
#####Reserved Words
Some words reserved for PostgreSQL database. Please try not to use those words table and column names. For complete list pleaser refer [here](http://www.postgresql.org/docs/9.3/static/sql-keywords-appendix.html)

**Solution:**<br/>*For table names;* **hibernate.prefix** *property will fix this issue. Write a table prefix avoiding from reserved word conflicts.*<br/>
*For column names;please open an issue on [Github](http://goo.gl/wGvbxr)*