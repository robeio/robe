# robe-hibernate
---
Bu modül YML dosyasından yapılandırma ayarlarını okur ve verilen paket isimleriyle ve özellikle belirtilen sınf isimleiyle birlikte Dropwizard Hibernate modulü ile eşleştirir.
## Motivasyon
YMl dosyası içerisinden basit bir şekilde ek `Base` sınıfları ile genişletilmiş `Entity` tarama sisteminin oluşturulması.

## Başlarken
robe-hibernate modülünü kullanmak için 4 adımı tamamlamanız gerekmektedir.
* Bağımlılıkları Ekleyin( Örnek:Maven)
```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-hibernate</artifactId>
  <version>0.5.0</version>
</dependency>
```
* YMl içerisindeki yapılandırmaları ekleyin (`scanPackages`,`entities` ve `dropwizard-hibernate` özellikleri)

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
* Sınıflarınızı normalde olduğu gibi `@Entity` annotation ekleyin. 

Hepsi bu kadar. Kullanmaya hazır.

## Detaylar
### DAO
`BaseDao<T>` sınıfından genişlettiğiniz sınıflarınız varsayılan olarak aşağıdaki özellikleri sizlere sunacaktır

* findAll
* findById
* create
* update
* delete
* flush
* merge
* detached

### Entity
`BaseEntity` sınıfından genişlettiğiniz sınıflarınıza aşağıdaki alanları ev dao kullanma desteği katacaktır.
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
