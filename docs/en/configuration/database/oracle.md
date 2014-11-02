##USING ROBE WITH ORACLE SERVER
###Pre-requirement
Oracle JDBC driver has to be downloaded from this [link](http://goo.gl/MuC8). Please find your Oracle version from the list. After JDBC driver downlaoded, import jar file to your project.

###Configuration
1. Open robe.yml file and find **hibernate** configuration header.
2. Change `driverClass` paremeter which located under the *database* sub-parameter as described below

		driverClass: oracle.jdbc.driver.OracleDriver

3. Fill your database `username`,`password` and `url` (database service URL) information e.g:

		user: dbUser
		password: dbPassword
		url: jdbc:oracle:thin:@IPADDRESS:PORT:SID

4. Important: `ValidationQuery` configuration parameter has to be added under the database parameter and value must be "**select 1 from dual**" .

		validationQuery: select 1 from dual
		
5. Find your oracle version and choose related dialect parameter for the hibernate.

		hibernate.dialect: org.hibernate.dialect.Oracle10gDialect
		
### Common Problems
#####Reserverd Words
Some words reserved for Oracle Database. Please try not to use those words table and column names. For complete list pleaser refer this [link](http://goo.gl/bcFrfw)

**Solution**:<br/> *For table names;* **hibarnate.prefix** *property will fix this issue. Write a table prefix for avoiding from reserved word conflicts.<br> For column names; please open an issue or send an email to Robe Developer Team.*
 