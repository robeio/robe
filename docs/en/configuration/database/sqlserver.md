##USING ROBE WITH MS SQL SERVER
###Pre-requirement
MS Sql Server JDBC driver has to be downloaded from [here](http://goo.gl/01Mk9m). Please find your SQL Server version from the list.Import jar file to your own project.

###Configuration
1. Open robe.yml file and find **hibernate** configuration header.
2. Change `driverClass` parameter which under the *database* sub-parameter described below;

		driverClass: com.microsoft.sqlserver.jdbc.SQLServerDriver

3. Fill `user`,`password` and `url` eg:

		user: robe
		password: robeio
		url: jdbc:sqlserver://192.168.1.58:1433;databaseName=robe
		

3. Change `dialect` parameter which under the *properties* sub-parameter described below(eg:sql server 2008);

		dialect: org.hibernate.dialect.SQLServer2008Dialect
