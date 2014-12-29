# Robe Tutorial
## Initial Configuration
* Create Development account 
* Install git,maven, intellij or eclipse, mysql
* Generate maven repository passwords.

## Project
* Create a new maven project
* Enable Autoimport for maven
* Copy .gitignore
* Modify `pom.xml` with the given 
 * Properties

 	```xml
 	<properties>
    	<dropwizard.version>0.7.1</dropwizard.version>
    	<robe.version>0.4.0</robe.version>
    	<mysql.version>5.1.26</mysql.version>
 	</properties>
 	```
 * Repository

 	```xml
 	<repositories>
    	<repository>
       	<id>Mebitech</id>
       	<name>Mebitech</name>
          <url>http://dev.mebitech.com/artifactory/Mebitech</url>
       </repository>
    </repositories>
 	```
 * Dependencies	
 
  	```xml
	<dependencies>
		<dependency>
			<groupId>io.robe</groupId>
			<artifactId>robe-admin</artifactId>
			<version>${robe.version}</version>
		</dependency>
		<dependency>
			<groupId>io.robe</groupId>
			<artifactId>robe-mail</artifactId>
			<version>${robe.version}</version>
		</dependency>
		<dependency>
			<groupId>io.robe</groupId>
			<artifactId>robe-guice</artifactId>
			<version>${robe.version}</version>
		</dependency>
		<dependency>
			<groupId>io.robe</groupId>
			<artifactId>robe-auth</artifactId>
			<version>${robe.version}</version>
		</dependency>
		<dependency>
			<groupId>io.robe</groupId>
			<artifactId>robe-hibernate</artifactId>
			<version>${robe.version}</version>
		</dependency>
		<dependency>
			<groupId>io.robe</groupId>
			<artifactId>robe-common</artifactId>
			<version>${robe.version}</version>
		</dependency>
		<dependency>
			<groupId>io.robe</groupId>
			<artifactId>robe-quartz</artifactId>
			<version>${robe.version}</version>
		</dependency>
		<dependency>
			<groupId>io.robe</groupId>
			<artifactId>robe-mq</artifactId>
			<version>${robe.version}</version>
		</dependency>
		<dependency>
			<groupId>io.dropwizard</groupId>
			<artifactId>dropwizard-assets</artifactId>
			<version>${dropwizard.version}</version>
		</dependency>
		<dependency>
			<groupId>io.dropwizard</groupId>
			<artifactId>dropwizard-views</artifactId>
			<version>${dropwizard.version}</version>
		</dependency>
		<dependency>
			<groupId>io.dropwizard</groupId>
			<artifactId>dropwizard-views-freemarker</artifactId>
			<version>0.7.0</version>
			<!--<version>${dropwizard.version}</version>-->
		</dependency>
		<dependency>
			<groupId>org.freemarker</groupId>
			<artifactId>freemarker</artifactId>
			<version>2.3.21</version>
		</dependency>
		<dependency>
			<groupId>com.sun.jersey</groupId>
			<artifactId>jersey-server</artifactId>
			<version>1.18.1</version>
		</dependency>
		<dependency>
			<groupId>com.sun.jersey.contribs</groupId>
			<artifactId>jersey-multipart</artifactId>
			<version>1.18.1</version>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>${mysql.version}</version>
		</dependency>
	</dependencies>
    ```
     	
 * Plug-ins
 
    ```xml
     <plugins>
	 	<plugin>
	 		<groupId>org.apache.maven.plugins</groupId>
	 		<artifactId>maven-compiler-plugin</artifactId>
	 		<version>3.1</version>
	 		<configuration>
	 			<source>1.7</source>
	 			<target>1.7</target>
	 		</configuration>
	 	</plugin>
	 	<plugin>
	 		<groupId>org.apache.maven.plugins</groupId>
	 		<artifactId>maven-jar-plugin</artifactId>
	 		<version>2.4</version>
	 		<configuration>
	 			<archive>
	 				<manifest>
	 					<addDefaultImplementationEntries>true</addDefaultImplementationEntries>
	 				</manifest>
	 			</archive>
	 		</configuration>
	 	</plugin>

	 	<plugin>
	 		<groupId>org.apache.maven.plugins</groupId>
	 		<artifactId>maven-source-plugin</artifactId>
	 		<version>2.1.2</version>
	 		<executions>
	 			<execution>
	 				<id>attach-sources</id>
	 				<goals>
	 					<goal>jar</goal>
	 				</goals>
	 			</execution>
	 		</executions>
	 	</plugin>
	 	<plugin>
	 		<groupId>org.apache.maven.plugins</groupId>
	 		<artifactId>maven-resources-plugin</artifactId>
	 		<version>2.5</version>
	 		<configuration>
	 			<outputDirectory/>
	 			<encoding>UTF-8</encoding>
	 		</configuration>
	 	</plugin>
	 </plugins>
  
    ```

* Create a `projectname.yml` file with the basic configuration. [sample.yml] (http://goo.gl/VxB7UM)
    ```yml
	# HTTP-specific options.
	server:
	  type: simple
	  applicationContextPath: /robe-sample
	  adminContextPath: /admin
	  connector:
	    type: http
	    port: 8080
	hibernate:
	   # Hibernate ScanPackages. Accepts multiple package names separated with comma ',' with a trailing  space ' '
	   scanPackages: [io.robe.admin.hibernate.entity,io.robe.quartz]
	   # Entities to be included. Accepts multiple class names separated with comma ',' with a trailing  space ' '
	   # entities: [io.robe.hibernate.entity.User]
	   database:
	     # the name of your JDBC driver
	     driverClass:  com.mysql.jdbc.Driver
	     # the username
	     user: root
	     # the password
	     password:
	     # the JDBC URL
	     url: jdbc:mysql://localhost:3306/robe
	     # Properties
	     properties:
	       charSet: UTF-8
	       dialect: org.hibernate.dialect.MySQL5InnoDBDialect
	       hibernate.hbm2ddl.auto: update
	logging:
	     # The default level of all loggers. Can be OFF, ERROR, WARN, INFO, DEBUG, TRACE, or ALL.
	     level: INFO
	     # Logger-specific levels.
	     #loggers:
	     #org.hibernate.SQL: ALL
	guice:
	  scanPackages: [io.robe]
	mail:
	 host: smtp.gmail.com
	 port: 587
	 auth: true
	 username: username@gmail.com
	 password: password
	 tlsssl: true
	# Quartz Scheduler configuration
	quartz:
	  instanceName: QuartzScheduler
	  threadPoolClass: org.quartz.simpl.SimpleThreadPool
	  threadCount: 10
	  threadPriority: 8
	  scanPackages: []
	  skipUpdateCheck: false
	  jobStore:
	    className: org.quartz.simpl.RAMJobStore
	# Message queue configuration
	messageQueue:
	  host: mq.example.com
	  port: 5673
	auth:
	  tokenKey: auth-token
	  domain: 127.0.0.1
	  path: /
	  maxage: 3600 #s
	  secure: false
	  poolSize: 4 # parallel with cpu cores
	  algorithm: PBEWithMD5AndTripleDES
	  serverPassword: auto # auto for uuid, if it is cluster use custom password
	asset:
	  resourcePath: /Users/serayuzgur/Development/robe-admin-ui/src/robe-admin-ui
	  uriPath: /ui/
	  indexFile: index.html
	  assetsName: io.robe.sample.ui
	  type: filesystem # use "classpath" or "filesystem"
    ```
* Create a class which extends `RobeApplication<RobeServiceConfiguration>`
	``` java
	public class SampleApplication extends RobeApplication<RobeServiceConfiguration> {
	    private static Logger LOGGER = LoggerFactory.getLogger(SampleApplication.class);

	    public static void main(String[] args) throws Exception {
	        new SampleApplication().run(args);
	    }


	    @Override
	    public void initialize(Bootstrap<RobeServiceConfiguration> bootstrap) {
	        super.initialize(bootstrap);
	    }

	    @Override
	    public void run(RobeServiceConfiguration configuration, Environment environment) throws Exception {
	        super.run(configuration, environment);
	    }
	}
	```

* Create a run configuration which will target Application class and have program arguments  `server sample.yml`

**NOTE: This is for server command which starts server**
* Create a run configuration which will target Application class and have program arguments  `initialize sample.yml`. Will prompt you the password for the admin.

**NOTE:This is for initialize command which creates standard user,role,permission,menu.etc for the projects which are extended from robe-admin.**

* Start MySql Server
* Run your application for the first time.