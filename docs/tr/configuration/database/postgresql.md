##ROBE POSTGRESQL İLE KULLANILMASI
###Ön Gereksinimler
Öncelikle size uygun olan PostgreSQL jdbc sürücüsünü [buradan](https://jdbc.postgresql.org/download.html) indirip projeye dahil ediniz veya Maven dependency olarak [buradan](http://mvnrepository.com/artifact/org.postgresql/postgresql) `pom.xml` dosyanıza dahil ediniz.

###Yapılandırma
1. `robe.yml` dosyasını açınız ve  **hibernate** başlığını bulunuz..
2. *database*  altındaki `driverClass` parametresini aşağıdaki ile değiştiriniz;

		driverClass: org.postgresql.Driver

3. `user`,`password` ve `url` yapılandırmalarını doldurunuz:

		user: dbUser
		password: dbPassword 
		url: jdbc:postgresql://IPADDRESS:PORT/tablename

3. *properties* altındaki `dialect` parametresini aşağıdaki ile değiştiriniz;

		dialect: org.hibernate.dialect.PostgreSQLDialect		
###Ortak Sorunlar
#####Özel Kelimeler
Bazı kelimeler PostgreSQL veritabanı için özel anlamlara gelmektedir. Tablo veya kolon isimlerinizi bu kelimelerden seçmemelisiniz. Detaylı listeye [buradan](http://www.postgresql.org/docs/9.3/static/sql-keywords-appendix.html) ulaşabilirsiniz.

**Çözüm:**<br/>*Tablo İsimleri İçin;* **hibernate.prefix** *alanını doldurarak oluşacak tablolarınızın isimleri başına ek bir kelime ile bu durumu çözebilirsiniz.Örnek:r_ seçtiğiniz zaman bütün tablolarınızı başına r_ gelecektir.*<br/>
*Kolon isimleri için; Lütfen robe içinde karşılaştığınız özel kelime var ise bize [Github](http://goo.gl/wGvbxr) üzerinden bildiriniz*