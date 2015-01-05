# robe-hibernate
---
This module reads the configuration file and maps the entity classes within the given packages and specifically stated class names to Dropwizard's hibernate module.
## Motivation
Creating an extended entity scanning system with additional `Base` classes for easy development.yml

## Getting started
You have to complete 3 steps in order to start using robe-hibernate bundle.
* Add dependency (Maven sample)
```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-hibernate</artifactId>
  <version>0.5.0</version>
</dependency>

* Add configuration (`scanPackages`,`entities` and `dropwizard-hibernate` properties)

```yml
hibernate:
   scanPackages: [io.robe.admin.hibernate.entity,io.robe.quartz]
   database:
     driverClass:  com.mysql.jdbc.Driver
     user: root
     password:
     url: jdbc:mysql://localhost:3306/robe
     properties:
       charSet: UTF-8
       dialect: org.hibernate.dialect.MySQL5InnoDBDialect
       hibernate.hbm2ddl.auto: update
```
* Annotate your class with `@Entity` as normal. 

All done it is visible by scanner and ready.

## Details
### DAO
Extending you dao classes from `BaseDao<T>` will add some default features to makeyou class as listed below.
* findAll
* findById
* create
* update
* delete
* flush
* merge
* detached

### Entity
Extending your entity from `BaseEntity` will add following fields and dao usage support
	
```java
@Id
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@GeneratedValue(generator = "system-uuid")
@Column( length = 32)
private String oid;

@Version
private long lastUpdated;
```
