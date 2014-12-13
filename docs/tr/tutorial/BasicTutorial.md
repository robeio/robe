# Robe Tutorial
## Başlangıç Konfigurasyonları
* Yazılım geliştirme hesabı oluşturun 
* Git,maven,mysql ve yazılım geliştirme için intellij veya eclipse indirin.
* Maven repo için şifre oluşturun.

## Proje
* Yeni bir maven projesi oluşturun
* Maven için otomatik import seçeneğini aktif edin
* .gitignore dosyasını kopyalayın
* `pom.xml` dosyanızı aşağıdaki özelliklere göre güncelleyin
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

* `projeadiniz.yml` adında basit konfigürasyona sahip bir dosya oluşturun. [ornek.yml] (http://dev.mebitech.com/confluence/display/ROBE/Sample+Robe+YML+Configuration)
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
    ```
*  `RobeApplication<RobeServiceConfiguration>` sınfını extend alan yeni bir sınıf yaratın.
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

* Uygulama sınıfını ayağa kaldıracak bir çalıştırma oluşturunuz ve program argümanlar olarak `server ornek.yml` veriniz.

**NOT: Bu komut sunucunun çalışmasını sağlamaktadır**
* Uygulama sınıfını ayağa kaldıracak bir çalıştırma oluşturunuz ve program argümanlar olarak `initialize ornek.yml` veriniz

**NOT: Bu robe-admin projesini extend alan projeler için standart olan kullanıcı,rol,izin,menü vs olan başlangıç ayarlarını yapmaktadır.**

* MySql Serverı çalıştırınız.
* Uygulamazı çalıştınız.