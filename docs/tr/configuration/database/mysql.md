##ROBE MYSQL İLE KULLANILMASI
###Ön Gereksinimler
Öncelikle size uygun olan mysql jdbc sürücüsünü [buradan](http://goo.gl/Ev05S7) indirip projeye dahil ediniz veya Maven depencies olarak [buradan](http://goo.gl/SQ3VcQ) `pom.xml` dosyanıza dahil ediniz.

###Yapılandırma
1. `robe.yml` dosyasını açınız ve  **hibernate** başlığını bulunuz..
2. *database*  altındaki `driverClass` parametresini aşağıdaki ile değiştiriniz;

		driverClass: com.mysql.jdbc.Driver

3. `user`,`password` ve `url` yapılandırmalarını doldurunuz:

		user: dbUser
		password: dbPassword 
		url: jdbc:mysql://IPADDRESS:PORT:/tabloadınız

3. *properties* altındaki `dialect` parametresini aşağıdaki iel değiştiriniz;

		dialect: org.hibernate.dialect.MySQL5InnoDBDialect
		
###Ortak Sorunlar
#####Özel Kelimeler
Bazı kelimeler Mysql veritabanı için özel anlamlara gelmektedir. Tablo veya kolon isimlerinizi bu kelimelerden seçmemelisiniz. Detaylı listeye [buradan](http://goo.gl/EnJx6Z) ulaşabilirsiniz.

**Çözüm:**<br/>*Tablo İsimleri İçin;* **hibernate.prefix** *alanını doldurarak oluşacak tablolarınızın isimleri başına ek bir kelime ile bu durumu çözebilirsiniz.Örnek:r_ seçtiğiniz zaman bütün tablolarınızı başına r_ gelecektir.*<br/>
*Kolon isimleri için; Lütfen robe içinde karşılaştığınız özel kelime var ise bize [Github](http://goo.gl/wGvbxr) üzerinden bildiriniz*