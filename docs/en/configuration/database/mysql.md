##USING ROBE WITH MYSQL
###Pre-requirement
Mysql JDBC driver has to be downloaded from [here](http://goo.gl/Ev05S7). Please find your Mysql version from the list.Import jar file to your own project or you can import maven depencies from [here](http://goo.gl/SQ3VcQ).

###Configuration
1. Open robe.yml file and find **hibernate** configuration header.
2. Change `driverClass` parameter which under the *database* sub-parameter described below;

		driverClass: com.mysql.jdbc.Driver

3. Fill `user`,`password` and `url` eg:

		user: dbUser
		password: dbPassword 
		url: jdbc:mysql://IPADDRESS:PORT:/tablename

3. Change `dialect` parameter which under the *properties* sub-parameter described below;

		dialect: org.hibernate.dialect.MySQL5InnoDBDialect
		
###Common Problems
#####Reserved Words
Some words reserved for Mysql database. Please try not to use those words table and column names. For complete list pleaser refer [here](http://goo.gl/EnJx6Z)

**Solution:**<br/>*For table names;* **hibernate.prefix** *property will fix this issue. Write a table prefix avoiding from reserved word conflicts.*<br/>
*For column names;please open an issue on [Github](http://goo.gl/wGvbxr)*