##USING ROBE WITH MS SQL SERVER
###Pre-requirement
MS Sql Server JDBC driver has to be downloaded from [here](http://goo.gl/01Mk9m). Please find your SQL Server version from the list.Import jar file to your own project.

###Configuration
1. Open robe.yml file and find **hibernate** configuration header.
2. Change `driverClass` parameter which under the *database* sub-parameter described below;

		driverClass: com.microsoft.sqlserver.jdbc.SQLServerDriver

3. Fill `user`,`password` and `url` eg:

		user: dbUser
		password: dbPassword 
		url: jdbc:sqlserver://IPADDRESS:PORT;databaseName=tablename

3. Change `dialect` parameter which under the *properties* sub-parameter described below(eg:sql server 2008);

		dialect: org.hibernate.dialect.SQLServer2008Dialect
		
###Common Problems
#####Reserved Words
Some words reserved for SQL server database. Please try not to use those words table and column names. For complete list pleaser refer [here](http://goo.gl/nqdIV7)

**Solution:**<br/>*For table names;* **hibernate.prefix** *property will fix this issue. Write a table prefix avoiding from reserved word conflicts.*<br/>
*For column names;please open an issue on [Github](http://goo.gl/wGvbxr)*