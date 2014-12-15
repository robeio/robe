##ROBE MS SQL SERVER İLE KULLANILMASI
###Ön Gereksinimler
Öncelikle size uygun olan sql server jdbc sürücüsünü [buradan](http://goo.gl/01Mk9m) indirip projeye dahil ediniz.

###Yapılandırmalar
1. `robe.yml` dosyasını açınız ve  **hibernate** başlığını bulunuz..
2. *database*  altındaki `driverClass` parametresini aşağıdaki ile değiştiriniz;

		driverClass: com.microsoft.sqlserver.jdbc.SQLServerDriver

3. `user`,`password` ve `url` yapılandırmalarını doldurunuz:

		user: dbUser
		password: dbPassword 
		url: jdbc:sqlserver://IPADDRESS:PORT;databaseName=tabloadınız

3. *properties* altındaki `dialect` parametresini sql server versiyonunuza uygun olarak aşağıdaki ile değiştiriniz;

		dialect: org.hibernate.dialect.SQLServer2008Dialect

###Ortak Sorunlar
#####Özel Kelimeler
Bazı kelimeler Mysql veritabanı için özel anlamlara gelmektedir. Tablo veya kolon isimlerinizi bu kelimelerden seçmemelisiniz. Detaylı listeye [buradan](http://goo.gl/nqdIV7) ulaşabilirsiniz.

**Çözüm:**<br/>*Tablo İsimleri İçin;* **hibernate.prefix** *alanını doldurarak oluşacak tablolarınızın isimleri başına ek bir kelime ile bu durumu çözebilirsiniz.Örnek:r_ seçtiğiniz zaman bütün tablolarınızı başına r_ gelecektir.*<br/>
*Kolon isimleri için; Lütfen robe içinde karşılaştığınız özel kelime var ise bize [Github](http://goo.gl/wGvbxr) üzerinden bildiriniz*