##ROBE ORACLE İLE KULLANILMASI
###Ön Gereksinimler
Orcale veri tabanınızın sürümüne göre jdbc sürücüsünü [buradan](http://goo.gl/MuC8) indiriniz. Daha sonra bu jar dosyasını projenize dahil ediniz.

###Yapılandırma
1. `robe.yml` dosyasını açınız ve  **hibernate** başlığını bulunuz..
2. *database*  altındaki `driverClass` parametresini aşağıdaki ile değiştiriniz;

		driverClass: oracle.jdbc.driver.OracleDriver

3. `user`,`password` ve `url` yapılandırmalarını doldurunuz:

		user: dbUser
		password: dbPassword
		url: jdbc:oracle:thin:@IPADDRESS:PORT:SID

4. Önemli Not: `ValidationQuery` isimli yapaılandrıma parametresini "**select 1 from dual**" olarak ekleyiniz.

		validationQuery: select 1 from dual
		
5. Oracle versiyonunuza göre aşağıdaki gibi `hibernate.dialect` parametresini doldurunuz.

		hibernate.dialect: org.hibernate.dialect.Oracle10gDialect
		
###Ortak Sorunlar
#####Özel Kelimeler
Bazı kelimeler Mysql veritabanı için özel anlamlara gelmektedir. Tablo veya kolon isimlerinizi bu kelimelerden seçmemelisiniz. Detaylı listeye [buradan](http://goo.gl/bcFrfw) ulaşabilirsiniz.

**Çözüm:**<br/>*Tablo İsimleri İçin;* **hibernate.prefix** *alanını doldurarak oluşacak tablolarınızın isimleri başına ek bir kelime ile bu durumu çözebilirsiniz. Örnek:r_ seçtiğiniz zaman bütün tablolarınızı başına r_ gelecektir.*<br/>
*Kolon isimleri için; Lütfen robe içinde karşılaştığınız özel kelime var ise bize [Github](http://goo.gl/wGvbxr) üzerinden bildiriniz*
 